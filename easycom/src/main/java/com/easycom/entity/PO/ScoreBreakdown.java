package com.easycom.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName score_breakdown
 */
@TableName(value ="score_breakdown")
@Data
@Builder
public class ScoreBreakdown implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;

    private Integer studentId;

    private double baseScore;

    private String baseScoreDetails;

    private double evaluationScore;

    private String evaluationScoreDetails;

    private double qualityScore;

    private String qualityScoreDetails;

    private double deductScore;

    private String deductScoresDetails;

    private double totalScore;

    private String totalScoreDetails;

    private Integer type;

    private Integer status;

    private String filePath;

    private static final long serialVersionUID = 1L;
}