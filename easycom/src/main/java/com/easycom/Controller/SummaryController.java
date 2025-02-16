package com.easycom.Controller;

import com.easycom.Service.ISummaryService;
import com.easycom.entity.VO.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/summary")
public class SummaryController {

    @Resource
    private ISummaryService summaryService;
    /**
     * 获取得分表
     */

    @RequestMapping("/showScore")
    public Result showScore(HttpServletRequest request){
        return summaryService.showScore(request);
    }

}
