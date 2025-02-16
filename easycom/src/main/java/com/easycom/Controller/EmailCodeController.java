package com.easycom.Controller;


import com.easycom.Service.IEmailCodeService;
import com.easycom.entity.VO.Result;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 邮箱验证码表 前端控制器
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-16
 */
@RestController
@RequestMapping("/email")
public class EmailCodeController {

    @Resource
    private IEmailCodeService emailCodeService;



}
