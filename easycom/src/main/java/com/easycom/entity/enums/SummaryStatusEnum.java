package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SummaryStatusEnum {
    CLASS_AUDIT(0,"班组审核中"),
    COUNSELLOR_AUDIT(1,"辅导员审核中"),
    SCHOOL_LEADERS_AUDIT(2,"校领导审核中"),
    SUCCESS(3,"审核成功"),
    RETURN(4,"退回");

    private final Integer status;
    private final String desc;
    //通过用户身份获取相应的Summary状态

    public static Integer getStatusByLevel(Integer level){
        for (SummaryStatusEnum value : SummaryStatusEnum.values()) {
            if (value.getStatus()+1 == level){
                return value.getStatus();
            }
        }
        return -1;
    }
}