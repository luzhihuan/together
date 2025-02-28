package com.easycom.Service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.easycom.Mapper.ScoreBreakdownMapper;
import com.easycom.Mapper.SummaryMapper;
import com.easycom.Mapper.UserInfoMapper;
import com.easycom.Service.IScoreBreakdownService;
import com.easycom.Utils.*;
import com.easycom.config.AppConfig;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.ScoreBreakdown;
import com.easycom.entity.PO.Summary;
import com.easycom.entity.PO.UserInfo;
import com.easycom.entity.VO.Result;
import com.easycom.entity.VO.ScoreBreakdownVO;
import com.easycom.entity.enums.ScoreBreakdownStatusEnum;
import com.easycom.entity.enums.ScoreBreakdownTypeEnum;
import com.easycom.entity.enums.SummaryStatusEnum;
import com.easycom.entity.enums.VerifyRegexEnum;
import com.easycom.exception.UserException;
import com.easycom.redis.RedisComponent;
import com.easycom.redis.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    @Resource
    private UserInfoMapper userInfoMapper;


    @Override
    public Result recordScore(HttpServletRequest request,Integer id,
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
        qualityScoreDetails = Optional.ofNullable(qualityScoreDetails).orElse("无");
        deductScoreDetails  = Optional.ofNullable(deductScoreDetails).orElse("无");

        //构建对象
        ScoreBreakdown scoreBreakdown = new ScoreBreakdown();
        scoreBreakdown.setUserId(tokenUserInfoDTO.getUserId());
        scoreBreakdown.setStudentId(DefaultParam.DEFAULT_STUDENT_ID);
        scoreBreakdown.setBaseScore(baseScore);
        scoreBreakdown.setBaseScoreDetails(baseScoreDetails);
        
        //只有德育才有互评分！
        if(type.equals(ScoreBreakdownTypeEnum.MORALITY.getType())){
            evaluationScoreDetails = Optional.ofNullable(evaluationScoreDetails).orElse("无");
            scoreBreakdown.setEvaluationScore(evaluationScore);
            scoreBreakdown.setEvaluationScoreDetails(evaluationScoreDetails);
        }
        scoreBreakdown.setQualityScore(qualityScore);
        scoreBreakdown.setQualityScoreDetails(qualityScoreDetails);
        scoreBreakdown.setDeductScore(deductScore);
        scoreBreakdown.setDeductScoresDetails(deductScoreDetails);
        scoreBreakdown.setTotalScore(totalScore);
        scoreBreakdown.setType(type);
        scoreBreakdown.setStatus(ScoreBreakdownStatusEnum.SENDING.getStatus());
        
        //如果id不为空且是有效整数，则为用户修改操作！
        if(id!=null && NumberUtil.isValidNumber(id) ){
            ScoreBreakdown db_scoreBreakdown = scoreBreakdownMapper.selectById(id);
            //如果查不到，或者查到了但不是这个学生的测评信息，或者修改的类型与数据库期望的类型不一致，均视为非法操作！
            if(db_scoreBreakdown == null){
                throw new UserException(DefaultParam.ILLEGAL_ACCESS);
            }
            if(!db_scoreBreakdown.getUserId().equals(tokenUserInfoDTO.getUserId()) || 
                !db_scoreBreakdown.getStudentId().equals(tokenUserInfoDTO.getStudentId())){
                throw new UserException(DefaultParam.ILLEGAL_ACCESS);
            }
            if(!db_scoreBreakdown.getType().equals(scoreBreakdown.getType())){
                throw new UserException(DefaultParam.ILLEGAL_ACCESS);
            }
            scoreBreakdown.setId(id);
        }

        //先将文件暂时存储到redis中
        if(files!=null){
            StringBuilder filePath = new StringBuilder();
            for (int i = 0; i < files.length; i++) {
                //获取文件后缀名
                String fileSuffix = UserHolder.getFileSuffix(files[i].getOriginalFilename());
                //如果文件不是图片，word，pdf类型
                if(!VerifyUtil.verify(VerifyRegexEnum.FILE_SUFFIX,fileSuffix)){
                    throw new UserException("文件格式不对！");
                }
                if(files[i].getOriginalFilename().length()>40){
                    throw new UserException("文件名长度请勿超过40！上传失败！");
                }

                filePath.append(files[i].getOriginalFilename());
                if(i < files.length -1){
                    filePath.append(",");
                }
                
                String typeName = ScoreBreakdownTypeEnum.getByType(type).getTypeName();
                
                //如果文件存在于服务器当中，则不需要保存到缓存中！
                String folder = appconfig.getProjectFolder() + DefaultParam.FILE_FOLDER_FILE + tokenUserInfoDTO.getUserId() + "/" + typeName;
                File fileFolder = new File(folder);
                List<String> server_fileNameList = new ArrayList<>();
                if(fileFolder.exists()){
                    server_fileNameList = FileUtil.listFileNames(fileFolder.getAbsolutePath());
                }

                // 将文件暂时存储到redis中
                // key命名规则 easycom:user:temp:{userId}:{typeName}:{count.fileSuffix}
                //将所有文件名保存到redis中，用一个列表，将用户上传的不同文件类型保存起来
                redisComponent.saveFile2Redis(tokenUserInfoDTO.getUserId(),typeName,files[i].getOriginalFilename(),files[i],server_fileNameList);
                
            }
            //设置文件路径为 name1.suf,name2.suf
            scoreBreakdown.setFilePath(filePath.toString());
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
//                scoreBreakdown == scoreBreakdownMapper.
                continue;
            }
            
            //设置信息为已提交
            scoreBreakdown.setStatus(ScoreBreakdownStatusEnum.FINISH.getStatus());
            if(StrUtil.isEmpty(scoreBreakdown.getFilePath())){
                scoreBreakdown.setFilePath("");
            }
            scoreBreakdownMapper.insertOrUpdate(scoreBreakdown);
            //记录每种类型的总分
            SummaryUtils.setInfo(typeEnum,summary,scoreBreakdown.getTotalScore());
        }
        
        //补充summary的信息，并录入到数据库当中
        summary.setStatus(SummaryStatusEnum.CLASS_AUDIT.getStatus());
        //总分计算
        SummaryUtils.setTotalScore(summary);
        summaryMapper.insertOrUpdate(summary);
        
        //将用户保存的临时文件保存到服务器文件夹../file/{userId}/{typeName}/中
        //由于是异步保存，需要等到事务提交完毕后，再进行保存!
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                redisComponent.saveUserFile2Folder(userId);
            }
        });
        
        return Result.ok("上传成功");
    }

//    @Override
//    public Result deleteScore(HttpServletRequest request,Integer type) {
//        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
//        RedisUtils.del(DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + type);
//        return Result.ok("删除成功");
//    }

    
//    @Override 
//    public Result beforeShowInfo(HttpServletRequest request, Integer type) {
//        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
//        return Result.ok((ScoreBreakdown)RedisUtils.get(DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + tokenUserInfoDTO.getUserId() + ":" + type));
//
//    }

    @Override
    public Result getUserScoreInfo(HttpServletRequest request) {
        TokenUserInfoDTO tokenUserInfoDTO = UserHolder.getTokenUserInfoDTO(request);
        String userId = tokenUserInfoDTO.getUserId();
        String studentId = tokenUserInfoDTO.getStudentId();
        UserInfo userInfo = userInfoMapper.selectByUserIdAndStudentId(userId, studentId);
        if(userInfo == null){
            return Result.fail(DefaultParam.PARAM_ERROR);
        }

        List<ScoreBreakdown> scoreBreakdownList = scoreBreakdownMapper.selectList(
                new LambdaQueryWrapper<ScoreBreakdown>()
                        .eq(ScoreBreakdown::getUserId, userId)
                        .eq(ScoreBreakdown::getStudentId, studentId)
        );
        
        if(scoreBreakdownList.size()!=5){
            return Result.fail(DefaultParam.PARAM_ERROR);
        }

        ArrayList<ScoreBreakdownVO> scoreBreakdownVOList = new ArrayList<>();  
        scoreBreakdownList.forEach(scoreBreakdown ->
                scoreBreakdownVOList.add(BeanUtil.copyProperties(scoreBreakdown,ScoreBreakdownVO.class)));
        return Result.ok(scoreBreakdownVOList);
    }
    
    
}




