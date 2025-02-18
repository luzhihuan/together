package com.easycom.aspect;

import com.easycom.Utils.DefaultParam;
import com.easycom.Utils.UserHolder;
import com.easycom.annotation.GlobalInterceptor;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.function.ServerRequest;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class GlobalAspectHandler {

    private String errorMsg = "";

    @Pointcut("@annotation(com.easycom.annotation.GlobalInterceptor)")
    public void requestInterceptor() {

    }

    @Before("requestInterceptor()")
    public void InterceptorDo(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
        if (interceptor == null) {
            return;
        }
        if (interceptor.checkLogin() || interceptor.checkAdmin()) {
            if (!checkLogin(interceptor.checkAdmin(), interceptor.checkIsFirstLogin())) {
                throw new UserException(errorMsg);
            }
        }
    }

    public boolean checkLogin(boolean isAdmin, boolean checkFirstLogin) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("token");
        if (token == null) {
            errorMsg = "请先登录!";
        }
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        if (tokenUserInfoDTO == null) {
            errorMsg = "请先登录";
            return false;
        }
        if (isAdmin && !tokenUserInfoDTO.getIsAdmin()) {
            errorMsg = "对不起！你不是管理员";
            return false;
        }
        if (tokenUserInfoDTO.getIsFirst() && checkFirstLogin) {
            errorMsg = "您是第一次登陆，请先更改密码！";
            return false;
        }
        return true;
    }


}
