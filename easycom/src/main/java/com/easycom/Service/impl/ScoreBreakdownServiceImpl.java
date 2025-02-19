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
import com.easycom.entity.enums.SummaryStatusEnum;
import com.easycom.exception.UserException;
import com.easycom.redis.RedisComponent;
import com.easycom.redis.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    private SummaryMapper summaryMapper;
    @Resource
    private RedisComponent redisComponent;
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
                .status(ScoreBreakdownStatusEnum.SENDING.getStatus()).build();
        
        //先将文件暂时存储到redis中
        if(files!=null){
            for (int i = 0; i < files.length; i++) {
                //获取文件后缀名
                String fileSuffix = UserHolder.getFileSuffix(files[i].getOriginalFilename());
                //如果文件不是图片类型
                if(!fileSuffix.equals(".png")&&!fileSuffix.equals(".PNG")&&
                        !fileSuffix.equals(".jpg")&&!fileSuffix.equals(".JPG")&&
                        !fileSuffix.equals(".JPEG")&&!fileSuffix.equals(".jpeg")&&
                        !fileSuffix.equals(".doc")&&!fileSuffix.equals(".pdf")&&
                        !fileSuffix.equals(".docx")
                ){
                    throw new UserException("文件格式不对！");
                }
                // 将文件暂时存储到redis中
                // key命名规则 easycom:user:temp:{userId}:{type}:{count.fileSuffix}
                redisComponent.saveProveInfo(tokenUserInfoDTO.getUserId(),ScoreBreakdownTypeEnum.getByType(type).getTypeName(),i+fileSuffix,files[i]);
                //将所有文件名保存到redis中，用一个列表，将用户上传的不同文件类型保存起来
                redisComponent.saveFileName2List(tokenUserInfoDTO.getUserId(),ScoreBreakdownTypeEnum.getByType(type).getTypeName(),i+fileSuffix);
            }
            
            //设置文件路径为 ../file/{userid}/{type}
            String filePath = DefaultParam.FILE_FOLDER_FILE+tokenUserInfoDTO.getUserId()+"/"+ScoreBreakdownTypeEnum.getByType(type).getTypeName();
            scoreBreakdown.setFilePath(filePath);
        }
        
        //每种类型的表，先暂时存到redis中
        redisComponent.saveScore(scoreBreakdown);
        
        return Result.ok("上传中");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveScore(HttpServletRequest request) {
        //建立一个summary的实例，记录各项总分
        Summary summary = new Summary();
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        summary.setUserId(tokenUserInfoDTO.getUserId());
        //遍历每一种类型的表，并将其写入数据库
        for (ScoreBreakdownTypeEnum typeEnum : ScoreBreakdownTypeEnum.values()) {
            ScoreBreakdown scoreBreakdown = (ScoreBreakdown) RedisUtils.get(
                    DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + typeEnum.getType());
            scoreBreakdown.setStatus(ScoreBreakdownStatusEnum.FINISH.getStatus());
            boolean b = scoreBreakdownMapper.insertOrUpdate(scoreBreakdown);
            //从缓存中获取到文件数量，存储到服务器文件夹中
            Integer proveInfoCount = redisComponent.getProveInfoCount(tokenUserInfoDTO.getUserId(), typeEnum.getTypeName());
            for (int i = 0; i < proveInfoCount; i++) {
                MultipartFile file = redisComponent.getUserTempFile(tokenUserInfoDTO.getUserId(),typeEnum.getTypeName(),i);
                //TODO 修改文件名
                //获取文件路径,并保存文件
                String filePath = scoreBreakdown.getFilePath();
                try {
                    file.transferTo(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!b) {
                //记录总分
                SummaryUtils.setInfo(typeEnum,summary,scoreBreakdown.getTotalScore());
                //用户填写完毕后，需要将所有临时文件删除，包括删除存储每一种类型的表
                RedisComponent.deleteAllScoreInfo(tokenUserInfoDTO.getUserId());
            }else {
                throw new UserException("上传失败，请重新检查！");
            }
        }

        //补充summary的信息，并录入到数据库当中
        summary.setStatus(SummaryStatusEnum.CLASS_AUDIT.getStatus());
        //总分计算
        SummaryUtils.setTotalScore(summary);
        summaryMapper.insertOrUpdate(summary);


        
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




