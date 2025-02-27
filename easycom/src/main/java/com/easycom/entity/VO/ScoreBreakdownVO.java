package com.easycom.entity.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 学生综测类型明细表
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-16
 */


public class ScoreBreakdownVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id每张表，都具有唯一性
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 基础分
     */
    private Double baseScore;

    /**
     * 基础分事项
     */
    private String baseScoreDetails;

    /**
     * 互评分，仅仅德育类型有
     */
    private Double evaluationScore;

    /**
     * 互评分事项
     */
    private String evaluationScoreDetails;

    /**
     * 加分项
     */
    private Double qualityScore;

    /**
     * 加分事项
     */
    private String qualityScoreDetails;

    /**
     * 扣分项
     */
    private Double deductScore;

    /**
     * 扣分事项
     */
    private String deductScoresDetails;

    /**
     * 总分
     */
    private Double totalScore;

    /**
     * 表的类型：1德  2智  3体  4美  5劳
     */
    private Integer type;



}
