package com.easycom.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class SummaryVo {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 学生ID
     */
    private String studentId;

    /**
     * 德育总分
     */
    private Double moralityScore;

    /**
     * 智育总分
     */
    private Double intellectualScore;

    /**
     * 体育总分
     */
    private Double sportScore;

    /**
     * 美育总分
     */
    private Double aestheticScore;

    /**
     * 劳育总分
     */
    private Double laborScore;

    /**
     * 总分
     */
    private Double totalScore;

    /**
     * 状态 0班组审核中 1辅导员审核中2校领导审核中 3审核通过 4退回
     */
    private Integer status;
}
