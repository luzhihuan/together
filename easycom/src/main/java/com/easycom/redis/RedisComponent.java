package com.easycom.redis;

import com.easycom.Utils.DefaultParam;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import org.springframework.stereotype.Component;

@Component
public class RedisComponent {
    public void saveTokenUserInfoDTO(TokenUserInfoDTO tokenUserInfoDTO){

        //存储两份信息
        // 第一份是通过token获取整一个DTO
        // 第二份是通过userId获取这个DTO的token

        RedisUtils.set(
                DefaultParam.REDIS_KEY_TOKEN+tokenUserInfoDTO.getToken(),
                tokenUserInfoDTO,
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_DAY
        );

        RedisUtils.set(
                DefaultParam.REDIS_KEY_TOKEN_USERID+tokenUserInfoDTO.getToken(),
                tokenUserInfoDTO.getUserId(),
                DefaultParam.REDIS_KEY_EXPIRE_TIME_ONE_DAY
        );
    }
}
