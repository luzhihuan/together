package com.easycom.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("register_code")
@Builder
public class RegisterCode {
    private static final long serialVersionUID = 1L;
    /**
     * 自增id
     */

    private Integer id;

    /**
     * 邀请码
     */

    private String code;

    /**
     * 状态0：未使用1：被使用 2：禁用
     */

    private Integer status;

    /**
     * 使用的邮箱
     */
    private String email;
}
