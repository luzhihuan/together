package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SummaryEnum {
    CLASS_AUDIT(0,"班组审核中"),
    COUNSELLOR_AUDIT(1,"辅导员审核中"),
    SCHOOL_LEADERS_AUDIT(2,"校领导审核中"),
    SUCCESS(3,"审核成功"),
    RETURN(4,"退回");

    private final Integer status;
    private final String desc;
}