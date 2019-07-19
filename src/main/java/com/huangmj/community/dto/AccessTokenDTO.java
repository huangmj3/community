package com.huangmj.community.dto;

import lombok.Data;

//传输参数大于两个的时候，将传输参数封装成对象去处理
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
