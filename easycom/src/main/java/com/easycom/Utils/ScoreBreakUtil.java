package com.easycom.Utils;

import com.easycom.entity.enums.ScoreBreakdownTypeEnum;
import org.springframework.web.ErrorResponseException;

public class ScoreBreakUtil {
    /**
     * 计算“德”类型（MORALITY）的总分。
     * <p>
     * 逻辑说明：
     * - 如果扣分（deductScore）大于或等于基础分（baseScore）、评价分（evaluationScore）和加分（qualityScore）的总和，
     * 则总分返回"0"。
     * - 否则，总分按照以下公式计算：
     * 总分 = 基础分 * 0.3 + 评价分 * 0.2 + 加分 * 0.5 - 扣分
     *
     * @param baseScore       基础分（String 类型，需转换为 Double 进行计算）
     * @param evaluationScore 评价分（String 类型，需转换为 Double 进行计算）
     * @param qualityScore    加分（String 类型，需转换为 Double 进行计算）
     * @param deductScore     扣分（String 类型，需转换为 Double 进行计算）
     * @return 计算后的总分（String 类型）
     */
    public static String getMoralityTotalCode(String baseScore, String evaluationScore,
                                              String qualityScore, String deductScore) {
        // 如果扣分大于或等于总分，则返回 "0"
        if (Double.parseDouble(deductScore) >= Double.parseDouble(baseScore) + Double.parseDouble(evaluationScore) + Double.parseDouble(qualityScore)) {
            return "0";
        }
        // 按照公式计算总分
        return String.valueOf(
                Double.parseDouble(baseScore) * 0.3 +
                        Double.parseDouble(evaluationScore) * 0.2 +
                        Double.parseDouble(qualityScore) * 0.5 -
                        Double.parseDouble(deductScore)
        );
    }

    /**
     * 计算“智”（INTELLECT）、“体”（PHYSICAL_EDUCATION）、“美”（AESTHETICS）和“劳”（LABOR）类型的总分。
     * <p>
     * 逻辑说明：
     * - 根据传入的类型（type），选择不同的权重公式进行计算。
     * - 总分公式为：
     * 总分 = 基础分 * 权重1 + 奖励分 * 权重2 - 扣分
     * - 如果类型不匹配，则返回 "-1" 表示错误。
     *
     * @param baseScore   基础分（String 类型，需转换为 Double 进行计算）
     * @param bonusPoints 加分（String 类型，需转换为 Double 进行计算）
     * @param deductScore 扣分（String 类型，需转换为 Double 进行计算）
     * @param type        分数类型（通过 ScoreBreakdownTypeEnum 获取）
     * @return 计算后的总分（String 类型）
     */
    public static String getOtherTotalCode(String baseScore, String bonusPoints, String deductScore, int type) {
        // 根据类型选择不同的权重公式
        if (type == ScoreBreakdownTypeEnum.INTELLECT.getType()) {
            // 智育：基础分权重0.8，奖励分权重0.2
            return String.valueOf(
                    Double.parseDouble(baseScore) * 0.8 +
                            Double.parseDouble(bonusPoints) * 0.2 -
                            Double.parseDouble(deductScore)
            );
        } else if (type == ScoreBreakdownTypeEnum.PHYSICAL_EDUCATION.getType()) {
            // 体育：基础分权重0.7，奖励分权重0.3
            return String.valueOf(
                    Double.parseDouble(baseScore) * 0.7 +
                            Double.parseDouble(bonusPoints) * 0.3 -
                            Double.parseDouble(deductScore)
            );
        } else if (type == ScoreBreakdownTypeEnum.AESTHETICS.getType() || type == ScoreBreakdownTypeEnum.LABOR.getType()) {
            // 美育和劳动：基础分权重0.5，奖励分权重0.5
            return String.valueOf(
                    Double.parseDouble(baseScore) * 0.5 +
                            Double.parseDouble(bonusPoints) * 0.5 -
                            Double.parseDouble(deductScore)
            );
        }
        // 如果类型不匹配，返回错误值 "-1"
        return "-1";
    }
}
