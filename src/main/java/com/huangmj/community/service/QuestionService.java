package com.huangmj.community.service;

import com.huangmj.community.dto.QuestionDTO;
import com.huangmj.community.mapper.QuestionMapper;
import com.huangmj.community.mapper.UserMapper;
import com.huangmj.community.model.Question;
import com.huangmj.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    public List<QuestionDTO> list() {
        //查询得到所有Question对象
        List<Question> questions =  questionMapper.list();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //工具类，通过变量名查找、反射,来进行赋值的Spring工具类
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        return questionDTOList;
    }
}
