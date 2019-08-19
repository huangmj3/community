package com.huangmj.community.controller;

import com.huangmj.community.dto.QuestionDTO;
import com.huangmj.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    //不需要关联spring上下文
    @Autowired(required = false)
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        //页面每次被打开，累加阅读数功能
        questionService.incView(id);
        //model+model.addAttribute放入前端界面
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
