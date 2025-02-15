package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ScoreBreakdownStatusEnum {
    SENDING(0,"正在提交"),
    FINISH(1,"完成提交");

    private final Integer status;
    private final String desc;
    public static ScoreBreakdownStatusEnum getByStatus(Integer status){
        for (ScoreBreakdownStatusEnum value : ScoreBreakdownStatusEnum.values()) {
            if(value.getStatus().equals(status)){
                return value;
            }
        }
        return null;
    }
}
