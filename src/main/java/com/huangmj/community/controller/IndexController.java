package com.huangmj.community.controller;

import com.huangmj.community.dao.UserMapper;
import com.huangmj.community.dto.PaginationDTO;
//import com.huangmj.community.mapper.UserMapper;
import com.huangmj.community.model.User;
import com.huangmj.community.model.UserExample;
import com.huangmj.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//网站首页控制器
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired(required = false)
    private UserMapper userMapper;

    //一个反斜杠代表根目录
    @GetMapping("/")
    public String index(HttpServletRequest request,
            Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size){
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
//                    User user = userMapper.findByToken(token);
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if(users.size() != 0){
//                        request.getSession().setAttribute("user",user);
                        request.getSession().setAttribute("user",users.get(0));

                    }
                    break;
                }
            }
        }
        PaginationDTO pagination = questionService.list(page, size);
        //model.addAttribute
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
