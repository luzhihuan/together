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
    private String userId;

    private String studentId;

    private double moralityScore;

    private double intellectualScore;

    private double sportScore;

    private double aestheticScore;


    private double laborScore;


    private double totalScore;

    private Integer status;


    private static final long serialVersionUID = 1L;
}