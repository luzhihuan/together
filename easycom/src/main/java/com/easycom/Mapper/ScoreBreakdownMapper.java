package com.easycom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easycom.entity.PO.ScoreBreakdown;
import org.apache.ibatis.annotations.Param;


/**
* @author 21597
* @description 针对表【score_breakdown】的数据库操作Mapper
* @createDate 2025-02-14 13:48:12
* @Entity com.easycom.po.ScoreBreakdown
*/
public interface ScoreBreakdownMapper extends BaseMapper<ScoreBreakdown> {
    
    ScoreBreakdown selectByUserIdAndStudentIdAndType(@Param("userId") String userId,
                                                     @Param("studentId") String studentId,
                                                     @Param("type") Integer type);
}




