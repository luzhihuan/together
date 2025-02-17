package com.easycom.Utils;

import cn.hutool.core.util.RandomUtil;
import com.easycom.entity.DTO.TokenUserInfoDTO;
import com.easycom.entity.VO.Result;
import com.easycom.exception.UserException;
import com.easycom.redis.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UserHolder {

    /**
     * 根据请求信息获取token进而获取用户信息
     * @param request   前端的请求信息
     * @return
     */
    public static TokenUserInfoDTO getTokenUserInfoDTO(HttpServletRequest request){
        String token = request.getHeader("token");
        TokenUserInfoDTO tokenUserInfoDTO = (TokenUserInfoDTO) RedisUtils.get(DefaultParam.REDIS_KEY_TOKEN + token);
        if(tokenUserInfoDTO == null){
            throw new UserException(DefaultParam.ILLEGAL_ACCESS);
        }
        return tokenUserInfoDTO;
    }

    public static String getUserIdByRandom(){
        return 'U'+RandomUtil.randomNumbers(11);
    }

    public static String getFileSuffix(String fileName){
        int index = fileName.lastIndexOf(".");
        if(index == -1){
            return "";
        }
        return fileName.substring(index);
    }

}
