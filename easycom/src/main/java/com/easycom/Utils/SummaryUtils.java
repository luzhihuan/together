package com.easycom.Utils;

import com.easycom.entity.PO.Summary;
import com.easycom.entity.VO.SummaryVo;
import com.easycom.entity.enums.ScoreBreakdownTypeEnum;


import java.beans.BeanProperty;
import java.util.ArrayList;
import java.util.List;

public class SummaryUtils {
    public static void setInfo(ScoreBreakdownTypeEnum typeEnum, Summary summary, Double totalScore) {
        if (typeEnum.equals(ScoreBreakdownTypeEnum.MORALITY)) {
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

    public static void setTotalScore(Summary summary) {
        
        /*        
            综合测评总成绩=德育测评成绩×20%＋智育测评成绩×60%＋体
                         育测评成绩×10%＋美育测评成绩×5%＋劳育测评成绩×5%
        */
//        summary.setTotalScore(summary.getMoralityScore() * 0.2
//                + summary.getIntellectualScore() * 0.6
//                + summary.getSportScore() * 0.1
//                + (summary.getAestheticScore() + summary.getLaborScore()) * 0.05);
        summary.setTotalScore(100d);
    }
    public static List<SummaryVo> getSummaryVos(List<Summary> summaries){
        /**
         * 通过数据库返回的Summarys生成Vo返回给前端
         */
        ArrayList<SummaryVo> summaryVos = new ArrayList<>();
        for (Summary summary : summaries) {
            SummaryVo summaryVo = new SummaryVo();
            summaryVo.setStudentId(summary.getStudentId());
            summaryVo.setUserId(summary.getUserId());
            summaryVo.setMoralityScore(summary.getMoralityScore());
            summaryVo.setIntellectualScore(summary.getIntellectualScore());
            summaryVo.setSportScore(summary.getSportScore());
            summaryVo.setAestheticScore(summary.getAestheticScore());
            summaryVo.setLaborScore(summary.getLaborScore());
            summaryVo.setTotalScore(summary.getTotalScore());
            summaryVo.setStatus(summaryVo.getStatus());
            summaryVos.add(summaryVo);
        }
        return summaryVos;
    }

}