package com.huangmj.community.mapper;

import com.huangmj.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionExtMapper {
    void incview(Question record);
}
