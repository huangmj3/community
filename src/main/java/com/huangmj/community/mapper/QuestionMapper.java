package com.huangmj.community.mapper;

import com.huangmj.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {
    @Insert("insert into (title,description,gmt_create,gmt_modified,creator,tag) values(#{})")
    public void create(Question question);
}
