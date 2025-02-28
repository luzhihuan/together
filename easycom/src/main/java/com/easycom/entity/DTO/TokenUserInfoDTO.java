package com.easycom.entity.DTO;

import lombok.Data;

@Data
public class TokenUserInfoDTO {
    private String nickName;
    private String userId;
    private String studentId;
    private Boolean isAdmin;
    private String token;
    private Integer level;
    private Boolean isFirst;
    private String classId;
    private String academyId;
    private String specId;
}
