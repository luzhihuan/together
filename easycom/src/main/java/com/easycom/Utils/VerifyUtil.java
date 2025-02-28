package com.easycom.Utils;

import cn.hutool.core.util.StrUtil;
import com.easycom.entity.VO.Result;
import com.easycom.entity.enums.VerifyRegexEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {

    public static boolean verify(String regex, String value) {
        if (StrUtil.isEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean verify(VerifyRegexEnum regex, String value) {
        return verify(regex.getRegex(), value);
    }

}
