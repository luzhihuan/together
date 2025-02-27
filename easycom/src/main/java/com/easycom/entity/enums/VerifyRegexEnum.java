package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VerifyRegexEnum {

    NO("","不校验"),
    STUDENT_ID("^[A-Z0-9]{9}$","学生学号"),
    IP("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])" +
            "(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}","IP地址"),
    POSITIVE_INTEGER("^[0-9]*[1-9][0-9]*$","正整数"),
    NUMBER_LETTER_UNDER_LINE("^\\w+&","由数字、26英文字母或下划线组成的字符串"),
    EMAIL("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$","邮箱"),
    PHONE("(1[0-9])\\d{9}$","手机号码"),
    COMMON("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$","数字，字母，中文，下划线"),
    PASSWORD("^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$","只能是数字，字母，特殊字符8-18位"),
    ACCOUNT("^[0-9a-zA-Z_]{1,20}$","字母开头，由数字，英文字母或下划线组成"),
    MONEY("^[0-9]+(.[0-9]{1,2})?$","金额");



    private final String regex;
    private final String desc;


}
