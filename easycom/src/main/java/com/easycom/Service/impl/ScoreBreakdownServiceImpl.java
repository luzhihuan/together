package com.easycom.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.easycom.Mapper.ScoreBreakdownMapper;
import com.easycom.Service.IScoreBreakdownService;
import com.easycom.Utils.DefaultParam;
import com.easycom.Utils.ScoreBreakUtil;
import com.easycom.Utils.UserHolder;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.ScoreBreakdown;
import com.easycom.entity.VO.Result;
import com.easycom.entity.enums.ScoreBreakdownStatusEnum;
import com.easycom.entity.enums.ScoreBreakdownTypeEnum;
import com.easycom.entity.enums.VerifyRegexEnum;
import com.easycom.exception.UserException;
import com.easycom.redis.RedisComponent;
import com.easycom.redis.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author 21597
 * @description 针对表【score_breakdown】的数据库操作Service实现
 * @createDate 2025-02-14 13:48:12
 */
@Service
public class ScoreBreakdownServiceImpl extends ServiceImpl<ScoreBreakdownMapper, ScoreBreakdown> implements IScoreBreakdownService {
    @Resource
    private ScoreBreakdownMapper scoreBreakdownMapper;
    @Resource
    private RedisComponent redisComponent;


    @Override
    public Result recordScore(HttpServletRequest request,
                              String baseScore, String baseScoreDetails,
                              String evaluationScore, String evaluationScoreDetails,
                              String qualityScore, String qualityScoreDetails,
                              String deductScore, String deductScoreDetails,
                              Integer type) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        String totalScore = "";

        //计算各个分数项
        if (type.equals(ScoreBreakdownTypeEnum.MORALITY.getType())) {
            totalScore = ScoreBreakUtil.getMoralityTotalCode(baseScore, evaluationScore, qualityScore, deductScore);
        } else {
            totalScore = ScoreBreakUtil.getOtherTotalCode(baseScore, evaluationScore, deductScore, type);
        }

        //处理空的情况！
        baseScoreDetails = Optional.ofNullable(baseScoreDetails).orElse("无");
        evaluationScoreDetails = Optional.ofNullable(evaluationScoreDetails).orElse("无");
        qualityScoreDetails = Optional.ofNullable(qualityScoreDetails).orElse("无");
        deductScoreDetails  = Optional.ofNullable(deductScoreDetails).orElse("无");

        //构建对象
        ScoreBreakdown scoreBreakdown = ScoreBreakdown.builder()
                .userId(tokenUserInfoDTO.getUserId())
                .baseScore(baseScore)
                .baseScoreDetails(baseScoreDetails)
                .evaluationScore(evaluationScore)
                .evaluationScoreDetails(evaluationScoreDetails)
                .qualityScore(qualityScore)
                .qualityScoreDetails(qualityScoreDetails)
                .deductScore(deductScore)
                .deductScoresDetails(deductScoreDetails)
                .totalScore(totalScore)
                .type(type)
                .status(ScoreBreakdownStatusEnum.SENDING.getStatus()).build();

        //每种类型的表，先暂时存到redis中
        redisComponent.saveScore(scoreBreakdown);

        return Result.ok("上传中");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveScore(HttpServletRequest request) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        //遍历每一种类型的表，并将其写入数据库
        for (ScoreBreakdownTypeEnum typeEnum : ScoreBreakdownTypeEnum.values()) {
            ScoreBreakdown scoreBreakdown = (ScoreBreakdown) RedisUtils.get(
                    DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + typeEnum.getType());
            scoreBreakdown.setStatus(ScoreBreakdownStatusEnum.FINISH.getStatus());
            boolean b = scoreBreakdownMapper.insertOrUpdate(scoreBreakdown);
            if (b) {
                throw new UserException("上传失败，请重新检查！");
            }
        }
        return Result.ok("上传成功");
    }

    @Override
    public Result deleteScore(HttpServletRequest request,Integer type) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        RedisUtils.del(DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + type);
        return Result.ok("删除成功");
    }

    @Override
    public Result beforeShowInfo(HttpServletRequest request, Integer type) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        return Result.ok((ScoreBreakdown)RedisUtils.get(DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + type));

    }

    @Override
    public Result afterShowInfo(HttpServletRequest request, Integer type) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);

        QueryWrapper<ScoreBreakdown> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",tokenUserInfoDTO.getUserId())
                    .eq("type",type);

        ScoreBreakdown scoreBreakdown = scoreBreakdownMapper.selectOne(queryWrapper);
        return Result.ok(scoreBreakdown);
    }
}




