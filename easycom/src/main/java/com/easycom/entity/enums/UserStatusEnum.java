package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatusEnum {

    DISABLE(0,"禁用"),
    ENABLE(1,"启用"),
    FIRST_TIME_LOGIN(2,"第一次登录");

    private final Integer status;
    private final String desc;

    public static UserStatusEnum getByStatus(Integer status){
        for (UserStatusEnum value : UserStatusEnum.values()) {
            if(value.getStatus().equals(status)){
                return value;
            }
        }
        return null;
    }


}
