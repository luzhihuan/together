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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author 21597
 * @description 针对表【score_breakdown】的数据库操作Service实现
 * @createDate 2025-02-14 13:48:12
 */
@Service
@Slf4j
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
        
        //TODO 查询数据库，如果发现已经存在有，那么说明是用户修改，执行修改逻辑！

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
        ScoreBreakdown scoreBreakdown = new ScoreBreakdown();
        scoreBreakdown.setUserId(tokenUserInfoDTO.getUserId());
        scoreBreakdown.setStudentId(DefaultParam.DEFAULT_STUDENT_ID);
        scoreBreakdown.setBaseScore(baseScore);
        scoreBreakdown.setBaseScoreDetails(baseScoreDetails);
        scoreBreakdown.setEvaluationScore(evaluationScore);
        scoreBreakdown.setEvaluationScoreDetails(evaluationScoreDetails);
        scoreBreakdown.setQualityScore(qualityScore);
        scoreBreakdown.setQualityScoreDetails(qualityScoreDetails);
        scoreBreakdown.setDeductScore(deductScore);
        scoreBreakdown.setDeductScoresDetails(deductScoreDetails);
        scoreBreakdown.setTotalScore(totalScore);
        scoreBreakdown.setType(type);
        scoreBreakdown.setStatus(ScoreBreakdownStatusEnum.SENDING.getStatus());
        

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
                // key命名规则 easycom:user:temp:{userId}:{typeName}:{count.fileSuffix}
                redisComponent.saveProveInfo(tokenUserInfoDTO.getUserId(),ScoreBreakdownTypeEnum.getByType(type).getTypeName(),DefaultParam.FILE_PROVE_NAME+i+fileSuffix,files[i]);
                //将所有文件名保存到redis中，用一个列表，将用户上传的不同文件类型保存起来
                redisComponent.saveFileName2List(tokenUserInfoDTO.getUserId(),ScoreBreakdownTypeEnum.getByType(type).getTypeName(),DefaultParam.FILE_PROVE_NAME+i+fileSuffix);
            }
            
            //设置文件路径为 ../file/{userid}/{typeName}
            String filePath = DefaultParam.FILE_FOLDER_FILE+tokenUserInfoDTO.getUserId()+"/"+ScoreBreakdownTypeEnum.getByType(type).getTypeName()+"/";
            scoreBreakdown.setFilePath(filePath);
        }
        
        //每种类型的表，先暂时存到redis中
        redisComponent.saveScore(scoreBreakdown);
        
        return Result.ok("上传中");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveScore(HttpServletRequest request) {

        
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        String userId = tokenUserInfoDTO.getUserId();

        //建立一个summary的实例，记录各项总分

        //TODO 后续查询条件应该为，userId和studentId
        Summary summary = summaryMapper.selectById(userId);
        if(summary == null ){
            summary = new Summary();
            summary.setUserId(userId);
            
            //TODO 这里应该改为传入的学生id
            summary.setStudentId(DefaultParam.DEFAULT_STUDENT_ID);
        }
        
        //遍历每一种类型的表，并将其写入数据库
        for (ScoreBreakdownTypeEnum typeEnum : ScoreBreakdownTypeEnum.values()) {
            
            //从redis获取用户存储的某一种类的测评信息
            ScoreBreakdown scoreBreakdown = (ScoreBreakdown) RedisUtils.get(
                    DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + userId + ":" + typeEnum.getType());
            
            //如果某种类型的评测信息不存在则不处理这种类型的测评信息
            if(scoreBreakdown==null){
                continue;
            }
            //设置信息为已提交
            scoreBreakdown.setStatus(ScoreBreakdownStatusEnum.FINISH.getStatus());
            
            boolean b = scoreBreakdownMapper.insertOrUpdate(scoreBreakdown);
            
            //从缓存中获取到文件数量，存储到服务器文件夹中
            List<String> fileNameList = redisComponent.getFileNameList(userId, typeEnum.getTypeName());
            
            //创建目录
            String folderName = appconfig.getProjectFolder()+DefaultParam.FILE_FOLDER_FILE + userId+"/";
            File folderFather = new File(folderName);
            if(!folderFather.exists()){
                folderFather.mkdir();
            }
            
            //带有类型名的目录
            String typeNameFolder = folderName + typeEnum.getTypeName()+"/";
            
            //将用户保存的临时文件保存到服务器文件夹../file/{userId}/{typeName}/中
            redisComponent.saveUserFile2Folder(userId,typeEnum.getTypeName(),fileNameList,typeNameFolder);

            if (b) {
                //记录每种类型的总分
                SummaryUtils.setInfo(typeEnum,summary,scoreBreakdown.getTotalScore());
                
                //用户提交所有类型信息完毕后，需要将所有临时文件删除，包括删除每一种类型的测评信息
                RedisComponent.deleteAllScoreInfo(userId);
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




