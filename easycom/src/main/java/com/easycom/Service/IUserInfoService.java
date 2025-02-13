package com.easycom.Service;

import com.easycom.entity.PO.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easycom.entity.VO.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpRequest;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-09
 */
public interface IUserInfoService extends IService<UserInfo> {
    Result getCheckCode();

    Result login( String checkCodeKey, String username, String password, String checkCode);

    Result regist(String checkCodeKey, String codeKey, String email, String password, String nickName);

    Result resetPassword(HttpServletRequest request, String password);
}
