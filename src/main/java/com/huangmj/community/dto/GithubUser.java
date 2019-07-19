package com.huangmj.community.dto;

import lombok.Data;

//github授权用户个人信息类
@Data
public class GithubUser {
    private Long id;
    private String name;
    private String bio;
    private String avatar_url;
}
