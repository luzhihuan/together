package com.easycom.Utils;

import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.VO.Result;
import com.easycom.exception.UserException;
import com.easycom.redis.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UserHolder {

    public static TokenUserInfoDTO getTokenUserInfoDTO(HttpServletRequest request){
        String token = request.getHeader("token");
        TokenUserInfoDTO tokenUserInfoDTO = (TokenUserInfoDTO) RedisUtils.get(DefaultParam.REDIS_KEY_TOKEN + token);
        if(tokenUserInfoDTO == null){
            throw new UserException(DefaultParam.ILLEGAL_ACCESS);
        }
        return tokenUserInfoDTO;

    }

}
