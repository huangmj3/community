package com.huangmj.community.controller;

import com.huangmj.community.dto.AccessTokenDTO;
import com.huangmj.community.dto.GithubUser;
import com.huangmj.community.mapper.UserMapper;
import com.huangmj.community.model.User;
import com.huangmj.community.provider.GithubProvider;
import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    //value注解使其自动去配置文件中读取数据
    //使用${}表示占位符数据读取
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    //没有映射就不会进行return查询
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser != null && githubUser.getId() != null){
            User user = new User();
            //生成UUID唯一识别码
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            //将用户信息插入数据库
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            //将user对象放入Session中
            request.getSession().setAttribute("user",githubUser);
            //地址重定向，否则会有其他字符串,同时redirect返回的是路径
            return "redirect:/";
            //登陆成功，写cookie和Session
        }else{
            //登录失败,重新登陆
            return "redirect:/";
        }
//        return "index";
    }
}
