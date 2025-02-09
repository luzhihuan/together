package com.easycom.config;


import com.easycom.entity.VO.Result;
import com.easycom.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class WebExceptionAdvice {

    @ExceptionHandler(UserException.class)
    public Result handleUserException(UserException e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return Result.fail("服务器异常");
    }

}
