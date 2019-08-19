package com.huangmj.community.dao;

import com.huangmj.community.model.Question;
import com.huangmj.community.model.QuestionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface QuestionExtMapper {
    int incView(Question question);
    int incComment(Question question);
}