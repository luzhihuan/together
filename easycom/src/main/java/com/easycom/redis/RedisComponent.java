package com.easycom.redis;

import com.easycom.Utils.DefaultParam;
import com.easycom.entity.DTO.SysSettingDTO;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.ScoreBreakdown;
import com.easycom.entity.enums.ScoreBreakdownTypeEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class RedisComponent {


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

    public void saveScore(ScoreBreakdown scoreBreakdown) {

        RedisUtils.set(
                DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + scoreBreakdown.getUserId() + ":" + scoreBreakdown.getType(),
                scoreBreakdown,
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_HOUR
        );
    }

    public void saveProveInfo(String userId, String typeName ,Integer count, MultipartFile file) {
        RedisUtils.set(
                DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId+":"+typeName+count,
                file,
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_HOUR
        );
    }
    public void saveProveInfoCount(String userId,String typeName, int length) {
        RedisUtils.set(
                DefaultParam.REDIS_KEY_USER_TEMP_FILE+userId+":"+typeName+"total",
                length,
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_HOUR
        );
    }

    public SysSettingDTO getSysSettingDTO() {
        SysSettingDTO sysSettingDTO = (SysSettingDTO) RedisUtils.get(DefaultParam.REDIS_KEY_SYS_SETTING);
        if (sysSettingDTO == null) {
            sysSettingDTO = new SysSettingDTO();
            RedisUtils.set(DefaultParam.REDIS_KEY_SYS_SETTING, sysSettingDTO,DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_DAY);
        }
        return sysSettingDTO;
    }

    public MultipartFile getUserTempFile(String userId,String typeName,Integer count) {
        return (MultipartFile) RedisUtils.get( DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId+":"+typeName+count);
    }
    public Integer getProveInfoCount(String userId,String typeName) {
        Integer i = (Integer) RedisUtils.get(DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + typeName + "total");
        if (i == null){
            return 0;
        }
        return i;
    }
    public static void deleteAllScoreInfo(String userId) {
        for (ScoreBreakdownTypeEnum value : ScoreBreakdownTypeEnum.values()) {
            Integer count = (Integer) RedisUtils.get(DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + value.getTypeName() + "total");
            for (int i = 0; i < count; i++) {
                //删除文件
                RedisUtils.del(DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId+":"+value.getTypeName()+i);
            }
            //删除文件数信息
            RedisUtils.del(DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId + ":" + value.getTypeName() + "total");
            //删除得分表
            RedisUtils.del(DefaultParam.REDIS_KEY_SCORE_BREAKDOWN_USERID + userId + ":" + value.getType());
        }
    }
}
