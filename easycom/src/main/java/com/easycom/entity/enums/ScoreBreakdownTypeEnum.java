package com.easycom.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ScoreBreakdownTypeEnum {
    MORALITY(1,"morality","德育"),
    INTELLECT(2,"intellect","智育"),
    PHYSICAL_EDUCATION(3,"physical_education","体育"),
    AESTHETICS(4,"aesthetics","美育"),
    LABOR(5,"labor","劳育");


    private final Integer type;
    private final String typeName;
    private final String desc;


    public static ScoreBreakdownTypeEnum getByType(Integer type){
        for (ScoreBreakdownTypeEnum value : ScoreBreakdownTypeEnum.values()) {
            if (value.getType().equals(type)){
                return value;
            }
        }
        return null;
    }
}
