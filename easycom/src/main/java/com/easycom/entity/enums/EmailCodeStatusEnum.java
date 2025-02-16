package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailCodeStatusEnum {

    NOT_USE(0,"未使用"),
    USED(1,"已使用");

    private final Integer status;
    private final String desc;

}
