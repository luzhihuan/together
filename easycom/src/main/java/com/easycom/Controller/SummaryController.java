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
    //展示总得分
    @RequestMapping("/showScore")
    public Result showScore(HttpServletRequest request){
        return summaryService.showScore(request);
    }

    /**
     *
     * @param request
     * @param current 页码
     * @param size 表的个数
     * @param classId 班级Id
     * @return
     */
    @RequestMapping("/showTask")
    public Result showTask(HttpServletRequest request,
                        Integer current,
                        Integer size,
                        String classId,
                        String specId,
                        String academyId){
        return summaryService.showTask(request,current,size,classId,specId,academyId);
    }

}
