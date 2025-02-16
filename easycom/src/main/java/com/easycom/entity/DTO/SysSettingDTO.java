package com.easycom.entity.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SysSettingDTO implements Serializable {
    private String registerMailTitle = "邮箱验证码";
    private String registerEmailContent = "您好，您的邮箱验证码是：%s，15分钟有效！";
}