package com.easycom.redis;

import com.easycom.Utils.DefaultParam;
import com.easycom.config.AppConfig;
import com.easycom.entity.DTO.SysSettingDTO;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.ScoreBreakdown;
import com.easycom.entity.enums.ScoreBreakdownStatusEnum;
import com.easycom.entity.enums.ScoreBreakdownTypeEnum;
import com.easycom.exception.UserException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RedisComponent {
    
    @Resource
    private AppConfig appConfig;


    public void saveTokenUserInfoDTO(TokenUserInfoDTO tokenUserInfoDTO) {

        //存储两份信息
        // 第一份是通过token获取整一个DTO
        // 第二份是通过userId获取这个DTO的token

        RedisUtils.set(
                DefaultParam.REDIS_KEY_TOKEN + tokenUserInfoDTO.getToken(),
                tokenUserInfoDTO,
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_DAY
        );

        RedisUtils.set(
                DefaultParam.REDIS_KEY_TOKEN_USERID + tokenUserInfoDTO.getUserId(),
                tokenUserInfoDTO.getToken(),
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_DAY
        );
    }

    //保存一种类型的表到redis
    public void saveScore(ScoreBreakdown scoreBreakdown) {

        RedisUtils.set(
                DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + scoreBreakdown.getUserId() + ":" + scoreBreakdown.getType(),
                scoreBreakdown,
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_HOUR
        );
    }

    //保存证明材料文件
    public void saveProveInfo(String userId, String typeName, String fileName, MultipartFile file) {
        try {
            RedisUtils.set(
                    DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeName + ":" + fileName,
                    file.getBytes(),
                    DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_HOUR
            );
        } catch (IOException e) {
            throw new RuntimeException("保存文件失败！");
        }
    }

    //保存上传的文件名
    public void saveFileName2List(String userId, String typeName, String fileName) {
        RedisUtils.lSet(
                DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeName + DefaultParam.REDIS_KEY_USER_TEMP_FILE_NAME_LIST,
                fileName,
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_HOUR
        );
    }

    public List<String> getFileNameList(String userId, String typeName) {
        List<Object> objects = RedisUtils.lGet(
                DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeName + DefaultParam.REDIS_KEY_USER_TEMP_FILE_NAME_LIST,
                0,
                -1
        );
        if (objects != null) {
            return objects.stream().map(o -> (String) o).collect(Collectors.toList());
        }
        return null;
    }

    @Async
    public void saveUserFile2Folder(String userId) {
        FileOutputStream fos = null;
        try{
            for (ScoreBreakdownTypeEnum typeEnum : ScoreBreakdownTypeEnum.values()) {
                //从缓存中获取到文件数量
                List<String> fileNameList = getFileNameList(userId, typeEnum.getTypeName());

                //创建目录 ../easycom/file/
                String folderName = appConfig.getProjectFolder()+DefaultParam.FILE_FOLDER_FILE + userId+"/";
                File folderFather = new File(folderName);
                if(!folderFather.exists()){
                    folderFather.mkdir();
                }
                //带有类型名的目录 ../easycom/file/{typeName}/
                String typeNameFolder = folderName + typeEnum.getTypeName()+"/";
                File folder = new File(typeNameFolder);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                for (String fileName : fileNameList) {
                    Object fileValue = RedisUtils.get(
                            DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeEnum.getTypeName() + ":" + fileName);
                    if (fileValue == null) {
                        log.info("文件，{}，不存在！", fileName);
                        continue;
                    }
                    byte[] files;
                    if (fileValue instanceof byte[]) {
                        files = (byte[]) fileValue;
                    } else if (fileValue instanceof String) {
                        files = Base64.getDecoder().decode((String) fileValue);
                    } else {
                        throw new IllegalArgumentException("Redis 中的数据类型不正确，预期为 byte[] 或 String，实际为：" + fileValue.getClass().getName());
                    }

                    //这是文件的写入路径
                    File file = new File(folder, fileName);
                    if (file.exists()) {
                        continue;
                    }
                    //将读取到的文件写入目标文件中
                    fos = new FileOutputStream(file);
                    fos.write(files);
                    fos.flush();
                }
            }
        }catch (IOException e){
            log.error("文件保存失败！因为：{}",e.getMessage());
            throw new UserException("文件保存失败！");
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                log.error("文件流关闭失败！因为："+e.getMessage());
            }
        }
    }


    public SysSettingDTO getSysSettingDTO() {
        SysSettingDTO sysSettingDTO = (SysSettingDTO) RedisUtils.get(DefaultParam.REDIS_KEY_SYS_SETTING);
        if (sysSettingDTO == null) {
            sysSettingDTO = new SysSettingDTO();
            RedisUtils.set(DefaultParam.REDIS_KEY_SYS_SETTING, sysSettingDTO, DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_DAY);
        }
        return sysSettingDTO;
    }

    public MultipartFile getUserTempFile(String userId, String typeName, Integer count) {
        return (MultipartFile) RedisUtils.get(DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeName + count);
    }


    public static void deleteAllScoreInfo(String userId) {
        RedisUtils.deleteKeysByPrefix(DefaultParam.REDIS_KEY_USER_TEMP_FILE+userId);
        RedisUtils.deleteKeysByPrefix(DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID+userId);
    }
}
