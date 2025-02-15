package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ScoreBreakdownTypeEnum {
    MORALITY(1,"德育"),
    INTELLECT(2,"智育"),
    PHYSICAL_EDUCATION(3,"体育"),
    AESTHETICS(4,"美育"),
    LABOR(5,"劳育");


    private final Integer type;
    private final String desc;

    public static ScoreBreakdownTypeEnum getType(Integer type){
        for (ScoreBreakdownTypeEnum value : ScoreBreakdownTypeEnum.values()) {
            if (value.getType().equals(type)){
                return value;
            }
        }
        return null;
    }
}
