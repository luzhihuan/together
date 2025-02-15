package com.easycom.Service.impl;

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
import com.easycom.redis.RedisComponent;
import com.easycom.redis.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

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
    public Result recordScore(HttpServletRequest request, String filePath, double baseScore, String baseScoreDetails, double evaluationScore, String evaluationScoreDetails, double qualityScore, String qualityScoreDetails, double deductScore, String deductScorDetails, Integer type, String totalScoreDetails) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        if (type == ScoreBreakdownTypeEnum.Morality.getType()) {
            totalCode = ScoreBreakUtil.getMoralityTotalCode(baseScore, evaluationScore, qualityScore, deductScore);
        } else {
            totalCode = ScoreBreakUtil.getOtherTotalCode(baseScore, evaluationScore, deductScore, type);
        }
        ScoreBreakdown build = ScoreBreakdown.builder().userId(tokenUserInfoDTO.getUserId()).
                baseScore(baseScore).baseScoreDetails(baseScoreDetails).
                evaluationScore(evaluationScore).evaluationScoreDetails(evaluationScoreDetails).
                qualityScore(qualityScore).qualityScoreDetails(qualityScoreDetails).
                deductScore(deductScore).deductScoresDetails(deductScorDetails).
                totalScore(totalCode).totalScoreDetails(totalScoreDetails).type(type).
                status(ScoreBreakdownStatusEnum.SENDING.getStatus()).filePath(filePath).build();

        try {
            redisComponent.saveSocre(build);
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
                int insert = scoreBreakdownMapper.insert(scoreBreakdown);
                if (insert == 1) {
                    RedisUtils.del(DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + i);
                }
            }
            return Result.ok("上传成功");
        } catch (Exception e) {
            return Result.fail("上传失败");
        }
    }

    @Override
    public Result updateScore(HttpServletRequest request, HashMap<String, Object> infos) {

    }
}




