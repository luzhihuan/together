package com.easycom.Utils;

import com.easycom.entity.PO.Summary;
import com.easycom.entity.enums.ScoreBreakdownTypeEnum;

public class SummaryUtils {
    public static void setInfo(ScoreBreakdownTypeEnum typeEnum,Summary summary,Double totalScore){
        if (typeEnum.equals(ScoreBreakdownTypeEnum.MORALITY)){
            summary.setMoralityScore(totalScore);
        } else if (typeEnum.equals(ScoreBreakdownTypeEnum.INTELLECT)) {
            summary.setIntellectualScore(totalScore);
        } else if (typeEnum.equals(ScoreBreakdownTypeEnum.PHYSICAL_EDUCATION)) {
            summary.setSportScore(totalScore);
        } else if (typeEnum.equals(ScoreBreakdownTypeEnum.AESTHETICS)) {
            summary.setAestheticScore(totalScore);
        } else if (typeEnum.equals(ScoreBreakdownTypeEnum.LABOR)) {
            summary.setLaborScore(totalScore);
        }
    }
}
