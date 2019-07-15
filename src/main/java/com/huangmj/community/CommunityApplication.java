package com.huangmj.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunityApplication {

    public static void main(String[] args) {
        System.out.println("starting run spring boot");
        SpringApplication.run(CommunityApplication.class, args);
        System.out.println("END");
    }

}
