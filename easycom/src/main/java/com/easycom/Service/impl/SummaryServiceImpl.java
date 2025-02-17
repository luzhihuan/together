package com.easycom.Service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.Mapper.SummaryMapper;
import com.easycom.Service.ISummaryService;
import com.easycom.Utils.UserHolder;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.Summary;
import com.easycom.entity.VO.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * @author 21597
 * @description 针对表【summary】的数据库操作Service实现
 * @createDate 2025-02-14 13:48:12
 */
@Service
public class SummaryServiceImpl extends ServiceImpl<SummaryMapper, Summary> implements ISummaryService {
    @Resource
    private SummaryMapper summaryMapper;

    @Override
    public Result showScore(HttpServletRequest request) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        Summary summary = summaryMapper.showScore(tokenUserInfoDTO.getUserId());
        return Result.ok(summary);
    }
}




