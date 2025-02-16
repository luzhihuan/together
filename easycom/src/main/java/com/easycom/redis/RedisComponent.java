package com.easycom.redis;

import com.easycom.Utils.DefaultParam;
import com.easycom.entity.DTO.SysSettingDTO;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.PO.ScoreBreakdown;
import org.springframework.stereotype.Component;

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
                DefaultParam.REDIS_KEY_TOKEN_USERID + tokenUserInfoDTO.getToken(),
                tokenUserInfoDTO.getUserId(),
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

    public void saveProveInfo(String userId, StringBuilder currentFilePath) {
        RedisUtils.set(
                DefaultParam.REDIS_KEY_USER_TEMP_FILE + userId,
                currentFilePath,
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_DAY
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
}
