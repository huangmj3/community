package com.huangmj.community.dto;

import lombok.Data;

//github授权用户个人信息类
@Data
public class GithubUser {
    private Long id;
    private String name;
    private String bio;
    //因为通过fastJson进行数据传输，fastJson自动进行驼峰映射，这样子命名更加符合java规范
    private String avatarUrl;
}
