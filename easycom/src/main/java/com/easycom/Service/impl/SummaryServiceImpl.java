package com.easycom.Service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easycom.Mapper.SummaryMapper;
import com.easycom.Service.ISummaryService;
import com.easycom.Utils.DefaultParam;
import com.easycom.Utils.SummaryUtils;
import com.easycom.Utils.UserHolder;
import com.easycom.Utils.VerifyUtil;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.Summary;
import com.easycom.entity.VO.Result;
import com.easycom.entity.VO.SummaryVo;
import com.easycom.entity.enums.SummaryStatusEnum;
import com.easycom.entity.enums.UserLevelEnum;
import com.easycom.entity.enums.VerifyRegexEnum;
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
    public Result check(HttpServletRequest request, Integer current, Integer size, String classId, String specId, String academyId) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        //根据身份判断是否具有该功能
        if (tokenUserInfoDTO.getLevel().equals(UserLevelEnum.NOMAL_USER.getLevel())) {
            return Result.fail("权限不足，无法审核");
        }
        //判断id是否符合规范
        if (!StrUtil.isEmpty(classId) || !StrUtil.isEmpty(specId) || !StrUtil.isEmpty(academyId)) {
            if (!VerifyUtil.verify(VerifyRegexEnum.CLASS_ID, classId) ||
                    !VerifyUtil.verify(VerifyRegexEnum.SPEC_ID, specId) ||
                    !VerifyUtil.verify(VerifyRegexEnum.ACADEMY_ID, academyId)) {
                return Result.fail("id格式错误");
            }
            
            //查询相对应等级所需要的审核的总分表
            QueryWrapper<Summary> summaryQueryWrapper = new QueryWrapper<>();
            summaryQueryWrapper.eq("status", SummaryStatusEnum.getStatusByLevel(tokenUserInfoDTO.getLevel()));
            List<String> studentIds = new ArrayList<>();
            if (tokenUserInfoDTO.getLevel().equals(UserLevelEnum.CLASS_AUDITORS.getLevel())){
                studentIds = SummaryUtils.getStudentIdsByClassId(classId);
            } else if (tokenUserInfoDTO.getLevel().equals(UserLevelEnum.COUNSELLOR_AUDITORS)) {
                studentIds = SummaryUtils.getStudentIdsBySpecId(specId);
            }else if (tokenUserInfoDTO.getLevel().equals(UserLevelEnum.SCHOOL_LEADER_AUDITORS)){
                studentIds = SummaryUtils.getStudentIdsByAcademyId(academyId);
            }
            // 进行分页查询
            IPage<Summary> page = new Page<>(current, size);
            List<Summary> summaryList = summaryMapper.selectList(page, summaryQueryWrapper);
            //生成vo的列表
            List<SummaryVo> summaryVos = new ArrayList<>();
            summaryList.forEach(summary -> summaryVos.add(BeanUtil.copyProperties(summary, SummaryVo.class)));

            if (summaryVos.isEmpty()) {
                return Result.ok("暂无审核任务");
            }
            return Result.ok(summaryVos, page);
        }
        return Result.ok("无法获取ID");
    }
}




