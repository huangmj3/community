package com.huangmj.community.controller;

import com.huangmj.community.mapper.QuestionMapper;
import com.huangmj.community.mapper.UserMapper;
import com.huangmj.community.model.Question;
import com.huangmj.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model) {
        //向前端模型中添加数据，便于数据的回显
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        User user = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0){
            if(cookies != null){
                for(Cookie cookie : cookies){
                    if(cookie.getName().equals("token")){
                        String token = cookie.getValue();
                        user = userMapper.findByToken(token);
                        if(user != null){
                            request.getSession().setAttribute("user",user);
                        }
                        break;
                    }
                }
            }
        }

        if(user == null){
            //通过model将信息传递至前端
            model.addAttribute("error","用户未登陆");
            return "publish";
        }

        //字段校验，前后端其实都应该有，防止前端绕过验证
        if(title == null || title.equals("")){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || description.equals("")){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag == null || tag.equals("")){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(System.currentTimeMillis());
        questionMapper.create(question);
        return "redirect:/";
    }
}
