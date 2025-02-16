package com.easycom.Mapper;

import com.easycom.entity.PO.EmailCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 邮箱验证码表 Mapper 接口
 * </p>
 *
 * @author luzhihuan
 * @since 2025-02-16
 */
public interface EmailCodeMapper extends BaseMapper<EmailCode> {

    void disableEmailCode(@Param("email") String email);

}
