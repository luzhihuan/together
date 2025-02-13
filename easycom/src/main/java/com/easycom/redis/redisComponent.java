package com.easycom.redis;

import com.easycom.Utils.DefaultParam;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import org.springframework.stereotype.Component;

@Component
public class RedisComponent {
    public void sendDTO(TokenUserInfoDTO tokenUserInfoDTO){
        RedisUtils.set(DefaultParam.REDIS_KEY_TOKEN+tokenUserInfoDTO.getToken(),tokenUserInfoDTO);
    }
}
