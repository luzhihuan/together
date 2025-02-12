package com.easycom.Service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import com.easycom.Utils.DefaultParam;
import com.easycom.Utils.JwtUtils;
import com.easycom.entity.PO.UserInfo;
import com.easycom.Mapper.UserInfoMapper;
import com.easycom.Service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.entity.VO.Result;
import com.easycom.redis.RedisUtils;
import io.springboot.captcha.SpecCaptcha;
import io.springboot.captcha.base.Captcha;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private JwtUtils jwtUtils;


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
    public Result login(String userId, String password) {
        UserInfo check = null;
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";//正则判断是否为邮箱
        if (userId.matches(emailRegex)) {
             check = userInfoMapper.selectByEmail(userId);
        }else {
             check = userInfoMapper.selectById(userId);
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
        String token = jwtUtils.generateToken(check.getUserId());
        return Result.ok(token);//生成token
    }

}
