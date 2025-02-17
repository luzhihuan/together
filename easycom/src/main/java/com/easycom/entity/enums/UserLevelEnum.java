package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserLevelEnum {
    NOMAL_USER(0,"普通用户"),
    CLASS_AUDITORS(1,"班审核员"),
    COUNSELLOR_AUDITORS(2,"辅导员审核员"),
    SCHOOL_LEADER_AUDITORS(3,"校领导审核员");
    private final Integer level;
    private final String desc;
}
