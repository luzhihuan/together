package com.easycom.Controller;


import com.easycom.Service.IUserInfoService;
import com.easycom.entity.VO.Result;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-09
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private IUserInfoService userInfoService;

    /**
     * 获取验证码
     * @return
     */
    @RequestMapping("/getCheckCode")
    public Result getCheckCode(){
        return userInfoService.getCheckCode();
    }

    /**
     * 用户登录逻辑
     * @param checkCodeKey  获取验证码的唯一标识
     * @param username      邮箱或者学号
     * @param password      密码
     * @param checkCode     前端传过来的验证码值
     * @return  用户传输信息
     */
    @RequestMapping("/login")
    public Result login(@NotEmpty String checkCodeKey,@NotEmpty @Max(12) String username,
                        @NotEmpty String password,@NotEmpty String checkCode){
        return userInfoService.login(checkCodeKey, username,password,checkCode);
    }
    @RequestMapping("/regist")
    public Result regist(@NotEmpty String userId,
                         @NotEmpty String email,
                         @NotEmpty String password,
                         @NotEmpty String nick_name){
        return userInfoService.regist(userId,email,password,nick_name);
    }

}
