package com.easycom.entity.DTO;

import lombok.Data;

@Data
public class TokenUserInfoDTO {
    private String nickName;
    private String userId;
    private Boolean isAdmin;
    private String token;
    private Integer level;
    private Boolean isFirst;

}
