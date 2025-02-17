package com.easycom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easycom.entity.PO.Summary;

/**
* @author 21597
* @description 针对表【summary】的数据库操作Mapper
* @createDate 2025-02-14 13:48:12
* @Entity com.easycom.po.Summary
*/
public interface SummaryMapper extends BaseMapper<Summary> {

    Summary showScore(String userId);
}




