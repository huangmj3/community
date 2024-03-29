package com.huangmj.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.huangmj.community.dao")
public class CommunityApplication {

    public static void main(String[] args) {
        System.out.println("starting run spring boot");
        SpringApplication.run(CommunityApplication.class, args);
        System.out.println("END");
    }

}
