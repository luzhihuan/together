package com.easycom.Controller;


import com.easycom.Service.IScoreBreakdownService;
import com.easycom.entity.VO.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/score")
public class ScoreBreakdownController {
    @Resource
    private IScoreBreakdownService scoreBreakdownService;


    
    @RequestMapping("/sendScore")
    public Result recordScore(HttpServletRequest request,
                              @NotEmpty String filePath, @NotEmpty double baseScore,
                              @NotEmpty @Max(200) String baseScoreDetails, @NotEmpty double evaluationScore,
                              @NotEmpty @Max(200) String evaluationScoreDetails, @NotEmpty double qualityScore,
                              @NotEmpty @Max(200) String qualityScoreDetails, @NotEmpty double deductScore,
                              @NotEmpty @Max(200) String deductScoreDetails, @NotEmpty Integer type, @NotEmpty String totalScoreDetails) {

        return scoreBreakdownService.recordScore(request,filePath, baseScore, baseScoreDetails,
                                                 evaluationScore, evaluationScoreDetails, qualityScore,
                                                 qualityScoreDetails, deductScore, deductScoreDetails, type,totalScoreDetails);

    }

    @RequestMapping("/saveScore")
    public Result saveScore(HttpServletRequest request){
        return scoreBreakdownService.saveScore(request);
    }

    @RequestMapping("/deleteScore")
    public Result deleteScore(HttpServletRequest request,Integer type){
        return scoreBreakdownService.deleteScore(request,type);
    }

    @RequestMapping("/beforeShowInfo")
    public Result beforeShowInfo(HttpServletRequest request,Integer type){
        return scoreBreakdownService.beforeShowInfo(request,type);
    }
    @RequestMapping("/afterShowInfo")
    public Result afterShowInfo(HttpServletRequest request,Integer type){
        return scoreBreakdownService.afterShowInfo(request,type);
    }
}
