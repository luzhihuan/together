package com.easycom.Controller;


import com.easycom.Service.IEmailCodeService;
import com.easycom.Service.IUserInfoService;
import com.easycom.Utils.DefaultParam;
import com.easycom.annotation.GlobalInterceptor;
import com.easycom.entity.VO.Result;
import com.easycom.entity.enums.VerifyRegexEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.*;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpRequest;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private IEmailCodeService emailCodeService;

    /**
     * 获取验证码
     * @return
     */
    @RequestMapping("/getCheckCode")
    public Result getCheckCode(Integer type){
        return userInfoService.getCheckCode(type);
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
    public Result login(@NotEmpty String checkCodeKey,@NotEmpty String username,
                        @NotEmpty String password,@NotEmpty String checkCode){
        return userInfoService.login(checkCodeKey, username,password,checkCode);
    }

    /**
     * 用户注册逻辑
     * @param email 邮箱
     * @param password  密码
     * @param nickName 昵称
     * @param checkCodeKey  验证码唯一标识
     * @param checkCode   用户输入的验证码
     * @param emailCode 邮箱验证码
     * @param registerCode 注册码用于确定审核员等级
     * @return
     */
    @RequestMapping("/register")
    public Result register(@NotEmpty String email,
                           @NotEmpty String password,
                           @NotEmpty String nickName,
                           @NotEmpty String checkCodeKey,
                           @NotEmpty String checkCode,
                           @NotEmpty String emailCode,
                           String registerCode){
        return userInfoService.register(checkCodeKey,checkCode,email,password,nickName,emailCode,registerCode);
    }

    /**
     * 登陆后的重置密码
     * @param request   前端请求信息
     * @param password  修改的密码
     * @return
     */
    @RequestMapping("/resetPassword")
    @GlobalInterceptor(checkIsFirstLogin = false)
    public Result resetPassword(HttpServletRequest request, 
                                @Pattern(regexp = DefaultParam.PASSWORD_VERIFY) String  password,
                                String email,String emailCode){
        return userInfoService.resetPassword(request,password,email,emailCode);
    }

    /**
     * 找回密码
     * @param password 修改的密码
     * @param checkCodeKey  验证码唯一标识
     * @param checkCode 验证码的值
     * @param emailCode 邮箱发送的验证码
     * @param email 用户邮箱
     * @return
     */
    @RequestMapping("/findPassword")
    public Result findPassword(@Pattern(regexp = DefaultParam.EMAIL_CHECK) String email,
                               @Pattern(regexp = DefaultParam.PASSWORD_VERIFY) String password,
                               @NotEmpty String checkCodeKey,
                               @NotEmpty String checkCode,
                               @NotEmpty String emailCode){
        return userInfoService.findPassword(email,password, checkCodeKey, checkCode, emailCode);
    }


    /**
     * 发送邮箱验证码
     * @param email
     * @param checkCode
     * @param checkCodeKey
     * @param type
     * @return
     */
    @RequestMapping("/sendCode")
    public Result sendCode(@NotEmpty @Pattern(regexp = DefaultParam.EMAIL_CHECK) String email,
                           @NotEmpty String checkCode,
                           @NotEmpty String checkCodeKey,
                           @NotNull Integer type){
        return emailCodeService.sendCode(email, checkCode, checkCodeKey, type);
    }

}
