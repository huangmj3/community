package com.huangmj.community.controller;

import com.huangmj.community.dto.CommentDTO;
import com.huangmj.community.dto.QuestionDTO;
import com.huangmj.community.enums.CommentTypeEnum;
import com.huangmj.community.service.CommentService;
import com.huangmj.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    //不需要关联spring上下文
    @Autowired(required = false)
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        //获取相关问题列表
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);

        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        //页面每次被打开，累加阅读数功能
        questionService.incView(id);
        //model+model.addAttribute放入前端界面
        model.addAttribute("question",questionDTO);
        //将评论放入界面
        model.addAttribute("comments",comments);
        //将相关问题放入界面
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
