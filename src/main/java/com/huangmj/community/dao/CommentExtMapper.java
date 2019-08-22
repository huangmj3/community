package com.huangmj.community.dao;

import com.huangmj.community.model.Comment;
import com.huangmj.community.model.Question;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}