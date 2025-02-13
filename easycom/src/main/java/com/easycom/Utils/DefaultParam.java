package com.easycom.Utils;

public class DefaultParam {

    public static final String PARAM_ERROR = "参数错误！";
    public static final String ILLEGAL_ACCESS = "非法访问！";


    public static final String REDIS_KEY_CHECK_CODE = "easycom:checkcode:";
    public static final String REDIS_KEY_TOKEN = "easycom:token:";
    public static final String REDIS_KEY_TOKEN_USERID = "easycom:token:userId";
    public static final Integer REDIS_KEY_EXPIRE_TIME_ONE_MIN = 60;
    public static final Integer REDIS_KEY_EXPIRE_TIME_ONE_DAY = REDIS_KEY_EXPIRE_TIME_ONE_MIN * 60 * 24;
}
