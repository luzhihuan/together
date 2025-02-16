package com.easycom.Utils;

import com.easycom.entity.enums.VerifyRegexEnum;

public class DefaultParam {

    public static final String PARAM_ERROR = "参数错误！";
    public static final String ILLEGAL_ACCESS = "非法访问！";


    public static final String REDIS_KEY_CHECK_CODE = "easycom:checkcode:";
    public static final String REDIS_KEY_TOKEN = "easycom:token:";
    public static final String REDIS_KEY_TOKEN_USERID = "easycom:token:userId";
    public static final Integer REDIS_KEY_EXPIRE_TIME_ONE_MIN = 60;
    public static final Integer REDIS_KEY_EXPIRE_TIME_ONE_DAY = REDIS_KEY_EXPIRE_TIME_ONE_MIN * 60 * 24;
    public static final Integer REDIS_KEY_EXPIRE_TIME_ONE_HOUR = REDIS_KEY_EXPIRE_TIME_ONE_MIN * 60;
    public static final String REDIS_KEY_SCORE_BREAKDOWN_USERID = "easycom:score:userId";
    public static final String REDIS_KEY_USER_TEMP_FILE = "easycom:user:temp:";

    public static final String PASSWORD_VERIFY = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}$";
    public static final String SCORE_LIMIT = "^([0-9]|[1-9][0-9]|100)$";

    public static final String FILE_FOLDER_FILE = "/file/";
}
