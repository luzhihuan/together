package com.easycom.config;


import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class AppConfig {

    @Value("${spring.mail.username:}")
    private String sendUserName;

    //项目的文件地址
    @Value("${project.folder}")
    private String projectFolder;

    //超级管理员
    @Value("${admin.emails}")
    private String adminEmail;

    public String getProjectFolder() {
        if (!StrUtil.isEmpty(projectFolder) && !projectFolder.endsWith("/")) {
            projectFolder = projectFolder + "/";
        }
        return projectFolder;
    }

}
