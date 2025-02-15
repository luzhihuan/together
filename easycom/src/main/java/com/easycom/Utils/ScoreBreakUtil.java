package com.easycom.Utils;

import com.easycom.entity.enums.ScoreBreakdownTypeEnum;
import org.springframework.web.ErrorResponseException;

public class ScoreBreakUtil {
    public static Double getMoralityTotalCode(double baseScore, double evaluationScore, double qualityScore, double deductScore) {
        if (deductScore >= baseScore + evaluationScore + qualityScore)
            return 0d;
        return baseScore * 0.3 + evaluationScore * 0.2 + qualityScore * 0.5 - deductScore;
    }

    public static Double getOtherTotalCode(double baseScore, double bonusPoints, double deductScore, int type) {
        if (type == ScoreBreakdownTypeEnum.INTELLECT.getType()) {
            return baseScore * 0.8 + bonusPoints * 0.2 - deductScore;
        } else if (type == ScoreBreakdownTypeEnum.PHYSICAL_EDUCATION.getType()) {
            return baseScore*0.7+bonusPoints*0.3 - deductScore;
        } else if (type == ScoreBreakdownTypeEnum.AESTHETICS.getType()||type == ScoreBreakdownTypeEnum.LABOR.getType()) {
            return baseScore*0.5 + bonusPoints*0.5 - deductScore;
        }
        return -1d;
    }
}
