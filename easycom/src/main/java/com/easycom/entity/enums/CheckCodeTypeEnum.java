package com.easycom.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CheckCodeTypeEnum {

    REGISTER(0,"注册时"),
    FIND_PASSWORD(1,"忘记密码时"),
    CHECK_EMAIL(2,"绑定邮箱时");

    private final Integer type;
    private final String desc;
}
