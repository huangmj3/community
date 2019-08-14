package com.huangmj.community.service;

import com.huangmj.community.dao.QuestionExtMapper;
import com.huangmj.community.dao.QuestionMapper;
import com.huangmj.community.dao.UserMapper;
import com.huangmj.community.dto.PaginationDTO;
import com.huangmj.community.dto.QuestionDTO;
//import com.huangmj.community.mapper.QuestionMapper;
//import com.huangmj.community.mapper.UserMapper;
import com.huangmj.community.exception.CustomizeErrorCode;
import com.huangmj.community.exception.CustomizeException;
//import com.huangmj.community.mapper.QuestionExtMapper;
import com.huangmj.community.model.Question;
import com.huangmj.community.model.QuestionExample;
import com.huangmj.community.model.User;
import com.huangmj.community.model.UserExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;
        //记录总条数
        QuestionExample questionExample = new QuestionExample();
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        //size * (page - 1)
        Integer offset = size * (page - 1);
        //查询得到所有Question对象
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            //通过用户ID查找得到用户
//            UserExample userExample = new UserExample();
//            userExample.createCriteria()
//                    .andIdEqualTo(question.getCreator());
//            List<User> users = userMapper.selectByExample(userExample);
//            User user = users.get(0);
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //工具类，通过变量名查找、反射,来进行赋值的Spring工具类
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;
        //记录总条数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

//        page = 1;

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        //size * (page - 1)
        Integer offset = size * (page - 1);
        //查询得到所有Question对象
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            //通过用户ID查找得到用户
//            UserExample userExample = new UserExample();
//            userExample.createCriteria()
//                    .andIdEqualTo(question.getCreator());
//            List<User> users = userMapper.selectByExample(userExample);
//            User user = users.get(0);
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //工具类，通过变量名查找、反射,来进行赋值的Spring工具类
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        //工具类，通过变量名查找、反射,来进行赋值的Spring工具类
        BeanUtils.copyProperties(question, questionDTO);
        //通过用户ID查找得到用户
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(userExample);
        User user = users.get(0);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //问题不存在，创建新问题
            questionMapper.insert(question);
        }
        else{
            //问题存在，更新问题
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            //updateByExampleSelective
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            //更新失败
            if(updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {
//        //通过selectByPrimaryKey去获得Question对象
//        Question question = questionMapper.selectByPrimaryKey(id);
//        Question updateQuestion = new Question();
//        updateQuestion.setViewCount(question.getViewCount() + 1);
//
//        QuestionExample questionExample = new QuestionExample();
//        questionExample.createCriteria()
//                .andIdEqualTo(id);

        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
//        questionMapper.updateByExampleSelective(updateQuestion, questionExample);
    }
}
