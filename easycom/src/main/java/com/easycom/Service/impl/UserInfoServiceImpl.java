package com.easycom.Service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import com.easycom.Mapper.RegisterCodeMapper;
import com.easycom.Service.IEmailCodeService;
import com.easycom.Utils.DefaultParam;
import com.easycom.Utils.UserHolder;
import com.easycom.Utils.VerifyUtil;
import com.easycom.config.AppConfig;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.RegisterCode;
import com.easycom.entity.PO.Summary;
import com.easycom.entity.PO.UserInfo;
import com.easycom.Mapper.UserInfoMapper;
import com.easycom.Service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.entity.VO.Result;
import com.easycom.entity.enums.RegisterCodeEnum;
import com.easycom.entity.enums.UserLevelEnum;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private RegisterCodeMapper registerCodeMapper;
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
        } else if (VerifyUtil.verify(VerifyRegexEnum.STUDENT_ID, username)) {
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
        tokenUserInfoDTO.setStudentId(check.getStudentId());
        tokenUserInfoDTO.setClassId(check.getClassId());
        tokenUserInfoDTO.setSpecId(check.getSpecId());
        tokenUserInfoDTO.setAcademyId(check.getAcademyId());

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
    @Transactional(rollbackFor = Exception.class)
    public Result register(String checkCodeKey, String checkCode,
                           String email, String password,
                           String nickName, String emailCode, String registerCode) {
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

            //检验邮箱验证码
            emailCodeService.checkCode(email, emailCode);
            UserInfo build = UserInfo.builder()
                    .userId(UserHolder.getUserIdByRandom())
                    .email(email)
                    .nickName(nickName)
                    .password(DigestUtil.md5Hex(password))
                    .status(UserStatusEnum.FIRST_TIME_LOGIN.getStatus())
                    .level(0)
                    .build();
            //根据邀请码设置相对应的用户身份，默认为普通用户
            if (registerCode != null && !registerCode.equals("")) {
                RegisterCode code = registerCodeMapper.selectByCode(registerCode);
                if (code.getStatus() == 0 && code.getEmail() == null) {
                    build.setLevel(code.getLevel());
                }
                code.setEmail(build.getEmail());
                code.setStatus(RegisterCodeEnum.IS_USE.getStatus());
                registerCodeMapper.updateById(code);
            }

            userInfoMapper.insert(build);

            return Result.ok("注册成功");

        } finally {
            RedisUtils.del(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey);
        }
    }


    @Override
    public Result resetPassword(HttpServletRequest request, String password, String email, String emailCode) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        UserInfo db_userInfo = userInfoMapper.selectById(tokenUserInfoDTO.getUserId());
        //检查原密码和新密码是否一样
        if (db_userInfo.getPassword().equals(DigestUtil.md5Hex(password))) {
            return Result.fail("密码未修改");
        }

        UserInfo userInfo = UserInfo.builder().build();
        userInfo.setPassword(DigestUtil.md5Hex(password));
        userInfo.setUserId(tokenUserInfoDTO.getUserId());

        //修改密码，如果第一次登录，强制要求绑定邮箱
        if (email != null && tokenUserInfoDTO.getIsFirst()) {
            userInfo.setEmail(email);
            tokenUserInfoDTO.setIsFirst(false);
            redisComponent.saveTokenUserInfoDTO(tokenUserInfoDTO);
            emailCodeService.checkCode(email, emailCode);
        }
        int update = userInfoMapper.updateById(userInfo);
        if (update > 0) {
            return Result.ok("密码修改成功");
        } else {
            return Result.fail("密码修改失败");
        }

    }

    @Override
    public Result findPassword(String email, String password, String checkCodeKey, String checkCode, String emailCode) {
        try {
            if (!RedisUtils.hasKey(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey)) {
                return Result.fail("图片验证码已过期，请重新获取！");
            }
            if (!checkCode.equalsIgnoreCase(
                    RedisUtils.get(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey).toString())) {
                return Result.fail("图片验证码不正确");
            }
            UserInfo db_userInfo = userInfoMapper.selectByEmail(email);
            if (db_userInfo == null) {
                return Result.fail("邮箱不存在！");
            }
            emailCodeService.checkCode(email, emailCode);
            UserInfo userInfo = UserInfo.builder().build();
            userInfo.setPassword(DigestUtil.md5Hex(password));
            userInfo.setUserId(db_userInfo.getUserId());
            userInfoMapper.updateById(userInfo);
            return Result.ok("修改成功！");
        } finally {
            RedisUtils.del(DefaultParam.REDIS_KEY_CHECK_CODE + checkCodeKey);
        }
    }

}
