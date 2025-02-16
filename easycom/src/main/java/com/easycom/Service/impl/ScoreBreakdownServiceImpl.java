package com.easycom.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.easycom.Mapper.ScoreBreakdownMapper;
import com.easycom.Mapper.SummaryMapper;
import com.easycom.Service.IScoreBreakdownService;
import com.easycom.Utils.DefaultParam;
import com.easycom.Utils.ScoreBreakUtil;
import com.easycom.Utils.SummaryUtils;
import com.easycom.Utils.UserHolder;
import com.easycom.config.AppConfig;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.ScoreBreakdown;
import com.easycom.entity.PO.Summary;
import com.easycom.entity.VO.Result;
import com.easycom.entity.enums.ScoreBreakdownStatusEnum;
import com.easycom.entity.enums.ScoreBreakdownTypeEnum;
import com.easycom.entity.enums.SummaryEnum;
import com.easycom.entity.enums.VerifyRegexEnum;
import com.easycom.exception.UserException;
import com.easycom.redis.RedisComponent;
import com.easycom.redis.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    @Resource
    private SummaryMapper summaryMapper;
    @Resource
    private AppConfig appconfig;


    @Override
    public Result recordScore(HttpServletRequest request,
                              Double baseScore, String baseScoreDetails,
                              Double evaluationScore, String evaluationScoreDetails,
                              Double qualityScore, String qualityScoreDetails,
                              Double deductScore, String deductScoreDetails,
                              Integer type, MultipartFile[] files) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        Double totalScore = 0d;

        //设置文件存储路径
        StringBuilder fileFolder = new StringBuilder(appconfig.getProjectFolder() + DefaultParam.FILE_FOLDER_FILE + tokenUserInfoDTO.getUserId());
        if(type.equals(ScoreBreakdownTypeEnum.MORALITY.getType())){
            fileFolder.append("/m/");
        }else if(type.equals(ScoreBreakdownTypeEnum.INTELLECT.getType())){
            fileFolder.append("/i/");
        }else if(type.equals(ScoreBreakdownTypeEnum.PHYSICAL_EDUCATION.getType())){
            fileFolder.append("/p/");
        }else if(type.equals(ScoreBreakdownTypeEnum.AESTHETICS.getType())){
            fileFolder.append("/a/");
        }else if(type.equals(ScoreBreakdownTypeEnum.LABOR.getType())){
            fileFolder.append("/l/");
        }else {
            return Result.fail(DefaultParam.PARAM_ERROR);
        }
        for (int i = 0; i < files.length; i++) {
            StringBuilder currentFilePath = fileFolder;
            //获取文件后缀名
            String fileSuffix = UserHolder.getFileSuffix(files[i].getName());
            //如果文件不是图片类型
            if(!fileSuffix.equals(".png")||!fileSuffix.equals(".PNG")||
                    !fileSuffix.equals(".jpg")||!fileSuffix.equals(".JPG")||
                    !fileSuffix.equals(".JPEG")||!fileSuffix.equals(".jpg")
            ){
                throw new UserException("文件格式不对！");
            }
            //所有上传的文件都命名为  1.图片后缀 2.图片后缀 3.图片后缀 等
            currentFilePath = currentFilePath.append(i+1).append(fileSuffix);
            //TODO 将文件暂时存储到redis中
            redisComponent.saveProveInfo(tokenUserInfoDTO.getUserId(),currentFilePath);
        }


        //设置文件路径规则为，如果用户传入5个文件，则为 /file/{userid}/5
        String filePath = DefaultParam.FILE_FOLDER_FILE+tokenUserInfoDTO.getUserId()+files.length;

        //计算各个分数项
        if (type.equals(ScoreBreakdownTypeEnum.MORALITY.getType())) {
            totalScore = ScoreBreakUtil.getMoralityTotalCode(baseScore, evaluationScore, qualityScore, deductScore);
        } else {
            totalScore = ScoreBreakUtil.getOtherTotalCode(baseScore, qualityScore, deductScore, type);
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
                .status(ScoreBreakdownStatusEnum.SENDING.getStatus())
                .filePath(filePath).build();

        //每种类型的表，先暂时存到redis中
        redisComponent.saveScore(scoreBreakdown);


        return Result.ok("上传中");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveScore(HttpServletRequest request) {
        Summary summary = new Summary();
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        //遍历每一种类型的表，并将其写入数据库
        for (ScoreBreakdownTypeEnum typeEnum : ScoreBreakdownTypeEnum.values()) {
            ScoreBreakdown scoreBreakdown = (ScoreBreakdown) RedisUtils.get(
                    DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + typeEnum.getType());
            scoreBreakdown.setStatus(ScoreBreakdownStatusEnum.FINISH.getStatus());
            boolean b = scoreBreakdownMapper.insertOrUpdate(scoreBreakdown);
            if (b) {
                //如果录入成功记录总分到summary中
                SummaryUtils.setInfo(typeEnum,summary,scoreBreakdown.getTotalScore());
            }else {
                throw new UserException("上传失败，请重新检查！");
            }
        }
        //补充summary的信息，并录入到数据库当中
        summary.setUserId(tokenUserInfoDTO.getUserId());
        summary.setStatus(SummaryEnum.CLASS_AUDIT.getStatus());
        //TODO 计算总分
        summaryMapper.save(summary);

        //TODO 从缓存中获取到用户所有临时文件，存储到服务器文件夹中

        //TODO 用户填写完毕后，需要将所有临时文件删除，包括删除存储每一种类型的表
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




