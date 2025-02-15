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
@RequestMapping("score")
public class ScoreBreakdownController {
    @Resource
    private IScoreBreakdownService scoreBreakdownService;


    @RequestMapping("sendScore")
    public Result recordScore(HttpServletRequest request,@NotEmpty String filePath, @NotEmpty double baseScore,
                              @NotEmpty @Max(200) String baseScoreDetails, @NotEmpty double evaluationScore,
                              @NotEmpty @Max(200) String evaluationScoreDetails, @NotEmpty double qualityScore,
                              @NotEmpty @Max(200) String qualityScoreDetails, @NotEmpty double deductScore,
                              @NotEmpty @Max(200) String deductScorDetails, @NotEmpty Integer type, @NotEmpty String totalScoreDetails) {

        return scoreBreakdownService.recordScore(request,filePath, baseScore, baseScoreDetails,
                                                 evaluationScore, evaluationScoreDetails, qualityScore,
                                                 qualityScoreDetails, deductScore, deductScorDetails, type,totalScoreDetails);

    }

    @RequestMapping("saveScore")
    public Result saveScore(HttpServletRequest request){
        return scoreBreakdownService.saveScore(request);
    }

    @RequestMapping("updateScore")
    public Result updateScore(HttpServletRequest request, HashMap<String, Object> infos){
        return scoreBreakdownService.updateScore(request,infos);
    }

}
