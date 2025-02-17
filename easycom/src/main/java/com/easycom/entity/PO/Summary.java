package com.easycom.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName summary
 */
@TableName(value ="summary")
@Data
public class Summary implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;

    private Integer studentId;

    private Integer moralityScore;

    private Integer intellectualScore;

    private Integer sportScore;

    private Integer aestheticScore;

    private Integer laborScore;

    private Integer totalScore;

    private Integer status;

    private static final long serialVersionUID = 1L;
}