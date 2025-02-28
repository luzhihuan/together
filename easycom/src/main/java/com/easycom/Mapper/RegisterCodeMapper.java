package com.easycom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easycom.entity.PO.RegisterCode;
import org.apache.ibatis.annotations.Param;


public interface RegisterCodeMapper extends BaseMapper<RegisterCode> {

    RegisterCode selectByCode(@Param("registerCode") String registerCode);
}