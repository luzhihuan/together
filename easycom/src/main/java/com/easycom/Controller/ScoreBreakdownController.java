package com.easycom.Controller;


import com.easycom.Service.IScoreBreakdownService;
import com.easycom.Utils.DefaultParam;
import com.easycom.annotation.GlobalInterceptor;
import com.easycom.entity.VO.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
     * @param id                    每种类型的测评信息的唯一id
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
    public Result recordScore(HttpServletRequest request,Integer id, @Max(100) Double baseScore,
                              @Length(max = 200) String baseScoreDetails, @Max(100) Double evaluationScore,
                              @Length(max = 200) String evaluationScoreDetails, @Max(100) Double qualityScore,
                              @Length(max = 200) String qualityScoreDetails, @Max(100) Double deductScore,
                              @Length(max = 200) String deductScoreDetails, @NotNull Integer type, MultipartFile[] files)  {

        return scoreBreakdownService.recordScore(request,id, baseScore, baseScoreDetails,
                evaluationScore, evaluationScoreDetails, qualityScore,
                qualityScoreDetails, deductScore, deductScoreDetails, type,files);

    }
    /**
     * 提取redis中的数据存储到数据库中
     * */
    @RequestMapping("/saveScore")
    @GlobalInterceptor
    public Result saveScore(HttpServletRequest request) {
        return scoreBreakdownService.saveScore(request);
    }
    
    
//    /**
//     * 删除redis中的存档
//     * */
//    @RequestMapping("/deleteScore")
//    @GlobalInterceptor
//    public Result deleteScore(HttpServletRequest request, Integer type) {
//        return scoreBreakdownService.deleteScore(request, type);
//    }
    
    
//    /**
//    * 在上传完成前访问redis展示数据
//    * */
//    @RequestMapping("/beforeShowInfo")
//    @GlobalInterceptor
//    public Result beforeShowInfo(HttpServletRequest request, Integer type) {
//        return scoreBreakdownService.beforeShowInfo(request, type);
//    }


    /**
     * 获取学生填写每一种类测评的信息，用于修改操作
     * @param request   前端信息
     * @return  每一种类型测评信息的一个列表
     */
    @RequestMapping("/getUserScoreInfo")
    @GlobalInterceptor
    public Result getUserScoreInfo(HttpServletRequest request ) {
        return scoreBreakdownService.getUserScoreInfo(request);
    }
    
    
}
