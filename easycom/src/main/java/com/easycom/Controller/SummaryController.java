package com.easycom.Controller;

import com.easycom.Service.ISummaryService;
import com.easycom.entity.VO.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/summary")
public class SummaryController {

    @Resource
    private ISummaryService summaryService;

    /**
     * 通过UserId查询summary
     * @param request
     * @return
     */

    @RequestMapping("/showScore")
    public Result showScore(HttpServletRequest request){
        return summaryService.showScore(request);
    }

//    @RequestMapping("/check")
//    public Result check(HttpServletRequest)

}
