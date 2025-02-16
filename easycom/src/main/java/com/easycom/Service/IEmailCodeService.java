package com.easycom.Service;

import com.easycom.Utils.DefaultParam;
import com.easycom.entity.PO.EmailCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easycom.entity.VO.Result;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 * <p>
 * 邮箱验证码表 服务类
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-16
 */
public interface IEmailCodeService extends IService<EmailCode> {

    Result sendCode(String email, String checkCode,
                    String checkCodeKey, Integer type);

    void checkCode(String email,String emailCode);
}
