package com.easycom.Controller;


import com.easycom.Service.IUserInfoService;
import com.easycom.entity.PO.UserInfo;
import com.easycom.entity.VO.Result;
import jakarta.annotation.Resource;
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

    @RequestMapping("/login")
    public Result login(@RequestParam UserInfo userInfo){
        return userInfoService.login(userInfo);
    }


}
