package com.easycom.Controller;


import com.easycom.Service.IScoreBreakdownService;
import com.easycom.Utils.DefaultParam;
import com.easycom.annotation.GlobalInterceptor;
import com.easycom.entity.VO.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/score")
public class ScoreBreakdownController {
    @Resource
    private IScoreBreakdownService scoreBreakdownService;


    /**
     * 填写每一种类型的表，并保存
     *
     * @param request                前端请求信息
     * @param baseScore              基础分
     * @param baseScoreDetails       基础分事项
     * @param evaluationScore        互评分
     * @param evaluationScoreDetails 互评分事项
     * @param qualityScore           加分
     * @param qualityScoreDetails    加分事项
     * @param deductScore            扣分
     * @param deductScoreDetails     扣分事项
     * @param type                   表的类型：1德  2智  3体  4美  5劳
     * @param files                  学生上传的证明材料
     * @return
     */
    @RequestMapping("/sendScore")
    @GlobalInterceptor
    public Result recordScore(HttpServletRequest request, @Pattern(regexp = DefaultParam.SCORE_LIMIT) String baseScore,
                              @Max(200) String baseScoreDetails, @Pattern(regexp = DefaultParam.SCORE_LIMIT) String evaluationScore,
                              @Max(200) String evaluationScoreDetails, @Pattern(regexp = DefaultParam.SCORE_LIMIT) String qualityScore,
                              @Max(200) String qualityScoreDetails, @Pattern(regexp = DefaultParam.SCORE_LIMIT) String deductScore,
                              @Max(200) String deductScoreDetails, @NotEmpty Integer type, MultipartFile[] files) {

        return scoreBreakdownService.recordScore(request, baseScore, baseScoreDetails,
                evaluationScore, evaluationScoreDetails, qualityScore,
                qualityScoreDetails, deductScore, deductScoreDetails, type,files);

    }

    @RequestMapping("/saveScore")
    @GlobalInterceptor
    public Result saveScore(HttpServletRequest request) {
        return scoreBreakdownService.saveScore(request);
    }

    @RequestMapping("/deleteScore")
    @GlobalInterceptor
    public Result deleteScore(HttpServletRequest request, Integer type) {
        return scoreBreakdownService.deleteScore(request, type);
    }

    @RequestMapping("/beforeShowInfo")
    @GlobalInterceptor
    public Result beforeShowInfo(HttpServletRequest request, Integer type) {
        return scoreBreakdownService.beforeShowInfo(request, type);
    }

    @RequestMapping("/afterShowInfo")
    @GlobalInterceptor
    public Result afterShowInfo(HttpServletRequest request, Integer type) {
        return scoreBreakdownService.afterShowInfo(request, type);
    }
}
