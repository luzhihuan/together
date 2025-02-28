package com.easycom.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easycom.entity.PO.Summary;
import com.easycom.entity.VO.Result;
import jakarta.servlet.http.HttpServletRequest;


/**
* @author 21597
* @description 针对表【summary】的数据库操作Service
* @createDate 2025-02-14 13:48:12
*/
public interface ISummaryService extends IService<Summary> {

    Result showScore(HttpServletRequest request);

    Result check(HttpServletRequest request, Integer current, Integer size, String classId, String specId, String academyId);
}
