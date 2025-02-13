package com.easycom.Service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.easycom.Utils.DefaultParam;
import com.easycom.Utils.VerifyUtil;
import com.easycom.config.AppConfig;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.UserInfo;
import com.easycom.Mapper.UserInfoMapper;
import com.easycom.Service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.entity.VO.Result;
import com.easycom.entity.enums.VerifyRegexEnum;
import com.easycom.redis.RedisComponent;
import com.easycom.redis.RedisUtils;
import io.springboot.captcha.SpecCaptcha;
import io.springboot.captcha.base.Captcha;
import jakarta.annotation.Resource;
import jodd.util.ArraysUtil;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
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


    @Override
    public Result getCheckCode() {
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
        RedisUtils.set(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey, checkCodeValue, DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_MIN);
        return Result.ok(res);
    }

    @Override
    public Result login(String checkCodeKey, String username, String password, String checkCode) {

        //第一步先检验验证码是否正确或者是否存在！
        if (RedisUtils.hasKey(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey)) {
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


        //判断一下是否为管理员
        String[] split = appConfig.getAdminEmail().split(",");
        if (ArraysUtil.contains(split, check.getEmail())) {
            tokenUserInfoDTO.setIsAdmin(true);
        } else {
            tokenUserInfoDTO.setIsAdmin(false);
        }


        //将dto保存到redis
        redisComponent.saveTokenUserInfoDTO(tokenUserInfoDTO);

        return Result.ok(tokenUserInfoDTO);

    }

    @Override
    public Result regist(String userId, String email, String password, String nickName) {
        try {
            userInfoMapper.insert(UserInfo.builder()
                                          .userId(userId)
                                          .email(email)
                                          .nickName(nickName)
                                          .password(password)
                                          .status(1)
                                          .build());
            return Result.ok("注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("注册失败");
        }
    }

}
