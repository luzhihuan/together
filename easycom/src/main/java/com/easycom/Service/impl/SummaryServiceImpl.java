package com.easycom.Service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.Mapper.SummaryMapper;
import com.easycom.Service.ISummaryService;
import com.easycom.Utils.SummaryUtils;
import com.easycom.Utils.UserHolder;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.Summary;
import com.easycom.entity.VO.Result;
import com.easycom.entity.VO.SummaryVo;
import com.easycom.entity.enums.SummaryStatusEnum;
import com.easycom.entity.enums.UserLevelEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        if (tokenUserInfoDTO.getLevel().equals(UserLevelEnum.NOMAL_USER.getLevel())){
            return Result.fail("权限不足，无法审核");
        }
        //查询相对应等级所需要的审核的总分表
        QueryWrapper<Summary> summaryQueryWrapper = new QueryWrapper<>();
        summaryQueryWrapper.eq("status", SummaryStatusEnum.getStatusByLevel(tokenUserInfoDTO.getLevel()));
        // TODO 假设每页显示5条记录,修改Ipage
        IPage<Summary> page = new Page<>(1, 15);
        List<Summary> summaryList = summaryMapper.selectList(page, summaryQueryWrapper);

        List<SummaryVo> summaryVos = new ArrayList<>();
        summaryList.forEach(summary -> summaryVos.add( BeanUtil.copyProperties(summary, SummaryVo.class)));
        if (summaryVos.isEmpty()){
            return Result.ok("暂无审核任务");
        }
        return Result.ok(summaryVos,page);
    }
}




