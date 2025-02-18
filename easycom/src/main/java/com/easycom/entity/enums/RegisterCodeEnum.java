package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RegisterCodeEnum {

    NOT_USE(0,"未被使用"),
    IS_USE(1,"被使用"),
    DISABLE(2,"禁用");

    private final Integer status;
    private final String desc;
}
