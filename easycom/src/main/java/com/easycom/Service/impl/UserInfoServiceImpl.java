package com.easycom.Service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.easycom.Service.IEmailCodeService;
import com.easycom.Utils.DefaultParam;
import com.easycom.Utils.UserHolder;
import com.easycom.Utils.VerifyUtil;
import com.easycom.config.AppConfig;
import com.easycom.entity.DTO.SysSettingDTO;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.UserInfo;
import com.easycom.Mapper.UserInfoMapper;
import com.easycom.Service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.entity.VO.Result;
import com.easycom.entity.enums.UserStatusEnum;
import com.easycom.entity.enums.VerifyRegexEnum;
import com.easycom.redis.RedisComponent;
import com.easycom.redis.RedisUtils;
import io.springboot.captcha.SpecCaptcha;
import io.springboot.captcha.base.Captcha;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jodd.util.ArraysUtil;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-09
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private AppConfig appConfig;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private IEmailCodeService emailCodeService;


    @Override
    public Result getCheckCode(Integer type) {
        Captcha captcha = new SpecCaptcha(120, 40, 5);
        captcha.setCharType(Captcha.TYPE_ONLY_CHAR);
        //验证码图片
        String checkCode = captcha.toBase64();
        //验证码的值
        String checkCodeValue = captcha.text().toLowerCase();
        //验证码唯一标识
        String checkCodeKey = UUID.fastUUID().toString(true);
        Map<String, String> res = new HashMap<>();
        res.put("checkCode", checkCode);
        res.put("checkCodeKey", checkCodeKey);
        if (type == null || type == 0) {
            RedisUtils.set(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey, checkCodeValue, DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_MIN);
        } else {
            RedisUtils.set(DefaultParam.REDIS_KEY_CHECK_CODE_EMAIL + checkCodeKey, checkCodeValue, DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_MIN);
        }
        return Result.ok(res);
    }

    @Override
    public Result login(String checkCodeKey, String username, String password, String checkCode) {

        //第一步先检验验证码是否正确或者是否存在！
        if (!RedisUtils.hasKey(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey)) {
            return Result.fail("图片验证码已过期，请重新获取！");
        }
        if (!checkCode.equalsIgnoreCase(
                RedisUtils.get(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey).toString())) {
            return Result.fail("图片验证码不正确");
        }

        UserInfo check = null;
        if (VerifyUtil.verify(VerifyRegexEnum.EMAIL, username)) {
            check = userInfoMapper.selectByEmail(username);
        } else if (VerifyUtil.verify(VerifyRegexEnum.ACCOUNT, username)) {
            check = userInfoMapper.selectById(username);
        } else {
            return Result.fail("用户名或密码错误！");
        }
        //根据用户ID查验账号
        if (check == null) {
            return Result.fail("用户ID不存在");
        }
        //检验账号是否被禁用 且 检验密码是否正确
        if (check.getStatus() == 0) {
            return Result.fail("账号已被禁用");
        }
        if (!check.getPassword().equals(password)) {
            return Result.fail("密码错误");
        }

        //生成token
        String token = UUID.fastUUID().toString(true);
        TokenUserInfoDTO tokenUserInfoDTO = new TokenUserInfoDTO();
        tokenUserInfoDTO.setUserId(check.getUserId());
        tokenUserInfoDTO.setToken(token);
        tokenUserInfoDTO.setNickName(check.getNickName());
        tokenUserInfoDTO.setLevel(check.getLevel());

        //判断一下是否为管理员
        String[] split = appConfig.getAdminEmail().split(",");
        if (ArraysUtil.contains(split, check.getEmail())) {
            tokenUserInfoDTO.setIsAdmin(true);
        } else {
            tokenUserInfoDTO.setIsAdmin(false);
        }


        //将dto保存到redis
        redisComponent.saveTokenUserInfoDTO(tokenUserInfoDTO);

        //第一次登录
//        if (check.getStatus().equals(UserStatusEnum.FIRST_TIME_LOGIN.getStatus())) {
//            return Result.firstLogin(tokenUserInfoDTO);
//        }

        return Result.ok(tokenUserInfoDTO);

    }

    @Override
    public Result register(String checkCodeKey, String checkCode, String email, String password, String nickName, String emailCode,String registerCode) {
        try {
            if (!RedisUtils.hasKey(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey)) {
                return Result.fail("图片验证码已过期，请重新获取！");
            }
            if (!checkCode.equalsIgnoreCase(
                    RedisUtils.get(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey).toString())) {
                return Result.fail("图片验证码不正确");
            }
            if (!VerifyUtil.verify(VerifyRegexEnum.PASSWORD, password)) {
                return Result.fail("密码格式错误");
            }
            if (userInfoMapper.selectByEmail(email) != null) {
                return Result.fail("邮箱已被注册");
            }
            //TODO 判断邀请码

            //检验邮箱验证码
            emailCodeService.checkCode(email, emailCode);

            userInfoMapper.insert(UserInfo.builder()
                    .userId(UserHolder.getUserIdByRandom())
                    .email(email)
                    .nickName(nickName)
                    .password(DigestUtil.md5Hex(password))
                    .status(UserStatusEnum.FIRST_TIME_LOGIN.getStatus())
                    .level(1)
                    .build());

            return Result.ok("注册成功");

        } finally {
            RedisUtils.del(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey);
        }
    }


    @Override
    public Result resetPassword(HttpServletRequest request, String password) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        UserInfo userInfo = userInfoMapper.selectById(tokenUserInfoDTO.getUserId());
        //检查原密码和新密码是否一样
        if (userInfo.getPassword().equals(DigestUtil.md5Hex(password))) {
            return Result.fail("密码未修改");
        }
        //修改密码
        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", tokenUserInfoDTO.getUserId()).set("password", password);
        int update = userInfoMapper.update(updateWrapper);
        if (update > 0) {
            return Result.ok("密码修改成功");
        } else {
            return Result.fail("密码修改失败");
        }

    }

    @Override
    public Result findPassword(String password, String checkCodeKey, String checkCode, String emailCode, String username) {
        //TODO 找回密码功能

        return null;
    }

}
