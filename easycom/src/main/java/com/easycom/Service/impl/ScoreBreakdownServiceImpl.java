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
import com.easycom.redis.RedisComponent;
import com.easycom.redis.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.HashMap;

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
    @Resource
    private Double totalCode;

    @Override
    public Result recordScore(HttpServletRequest request, String filePath,
                              double baseScore, String baseScoreDetails,
                              double evaluationScore, String evaluationScoreDetails,
                              double qualityScore, String qualityScoreDetails,
                              double deductScore, String deductScoreDetails,
                              Integer type, String totalScoreDetails) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        if (type.equals(ScoreBreakdownTypeEnum.MORALITY.getType())) {
            totalCode = ScoreBreakUtil.getMoralityTotalCode(baseScore, evaluationScore, qualityScore, deductScore);
        } else {
            totalCode = ScoreBreakUtil.getOtherTotalCode(baseScore, evaluationScore, deductScore, type);
        }
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
                .totalScore(totalCode)
                .totalScoreDetails(totalScoreDetails)
                .type(type)
                .status(ScoreBreakdownStatusEnum.SENDING.getStatus())
                .filePath(filePath).build();

        try {
            redisComponent.saveScore(scoreBreakdown);
            return Result.ok("上传中");
        } catch (Exception e) {
            return Result.fail("上传失败");
        }
    }

    @Override
    public Result saveScore(HttpServletRequest request) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        ScoreBreakdown scoreBreakdown = null;
        try {
            for (int i = 1; i <= 5; i++) {
                scoreBreakdown = (ScoreBreakdown) RedisUtils.get(DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + i);
                scoreBreakdown.setStatus(ScoreBreakdownStatusEnum.FINISH.getStatus());
                boolean b = scoreBreakdownMapper.insertOrUpdate(scoreBreakdown);
                if (b) {
                    RedisUtils.del(DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + i);
                }
            }
            return Result.ok("上传成功");
        } catch (Exception e) {
            return Result.fail("上传失败");
        }
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




