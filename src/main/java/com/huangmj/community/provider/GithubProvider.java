package com.huangmj.community.provider;

import com.alibaba.fastjson.JSON;
import com.huangmj.community.dto.AccessTokenDTO;
import com.huangmj.community.dto.GithubUser;
import org.springframework.stereotype.Component;
import okhttp3.*;
import okhttp3.MediaType;

import java.io.IOException;


//controller注解是把当前类作为路由API的承载者
// 而component注解则是将该类初始化到spring容器的上下文
//自动实例化，autowired,spring IOC
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //JSON数据通过string方法转换为字符串
            String string = response.body().string();
            System.out.println(string);
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try{
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);
            return githubUser;
        }catch (IOException e){
        }
        return null;
    }
}
