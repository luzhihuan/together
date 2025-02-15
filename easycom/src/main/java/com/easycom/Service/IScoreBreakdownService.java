package com.easycom.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easycom.entity.PO.ScoreBreakdown;
import com.easycom.entity.VO.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;


/**
* @author 21597
* @description 针对表【score_breakdown】的数据库操作Service
* @createDate 2025-02-14 13:48:12
*/
public interface IScoreBreakdownService extends IService<ScoreBreakdown> {

    Result recordScore(HttpServletRequest request,
                       String baseScore, String baseScoreDetails,
                       String evaluationScore, String evaluationScoreDetails,
                       String qualityScore, String qualityScoreDetails,
                       String deductScore, String deductScoreDetails, Integer type);

    Result saveScore(HttpServletRequest request);


    Result deleteScore(HttpServletRequest request,Integer type);

    Result beforeShowInfo(HttpServletRequest request, Integer type);

    Result afterShowInfo(HttpServletRequest request, Integer type);
}
