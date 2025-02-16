package com.easycom.Controller;

import com.easycom.Service.ISummaryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/summary")
public class SummaryController {

    @Resource
    private ISummaryService summaryService;

//    @RequestMapping("/")
}
