package com.easycom.Service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.Mapper.SummaryMapper;
import com.easycom.Service.ISummaryService;
import com.easycom.Utils.UserHolder;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.Summary;
import com.easycom.entity.VO.Result;
import com.easycom.entity.enums.SummaryStatusEnum;
import com.easycom.entity.enums.UserLevelEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Result check(HttpServletRequest request) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        //根据身份判断是否具有该功能
        if (!tokenUserInfoDTO.getLevel().equals(UserLevelEnum.NOMAL_USER.getLevel())
                ||tokenUserInfoDTO.getLevel()<UserLevelEnum.NOMAL_USER.getLevel()
                ||tokenUserInfoDTO.getLevel()>UserLevelEnum.SCHOOL_LEADER_AUDITORS.getLevel()){
            return Result.fail("权限不足，无法审核");
        }
        //查询相对应等级所需要的审核的总分表
        QueryWrapper<Summary> summaryQueryWrapper = new QueryWrapper<>();
        summaryQueryWrapper.eq("status", SummaryStatusEnum.getStatusByLevel(tokenUserInfoDTO.getLevel()));
        List<Summary> summaries = summaryMapper.selectList(summaryQueryWrapper);
        return Result.ok(summaries);
    }
}




