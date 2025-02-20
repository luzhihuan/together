package com.easycom.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("summary")
public class Summary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表的自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
