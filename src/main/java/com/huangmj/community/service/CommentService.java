package com.huangmj.community.service;

import com.huangmj.community.dao.CommentMapper;
import com.huangmj.community.dao.QuestionExtMapper;
import com.huangmj.community.dao.QuestionMapper;
import com.huangmj.community.dao.UserMapper;
import com.huangmj.community.dto.CommentDTO;
import com.huangmj.community.enums.CommentTypeEnum;
import com.huangmj.community.exception.CustomizeErrorCode;
import com.huangmj.community.exception.CustomizeException;
import com.huangmj.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

	@Autowired(required = false)
	private CommentMapper commentMapper;

	@Autowired(required = false)
	private QuestionMapper questionMapper;

	@Autowired(required = false)
	private QuestionExtMapper questionExtMapper;

	@Autowired(required = false)
	private UserMapper userMapper;

	//添加事务，同一事务要么全部成功，要不全部失败，因为会进行回滚操作
	@Transactional
	public void insert(Comment comment) {
		//未选择任何问题或评论进行回复
		if (comment.getParentId() == null || comment.getParentId() == 0) {
			throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
		}
		//评论类型错误或不存在
		if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
			throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
		}

		if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
			//回复评论,通过ParentId进行了问题查找
			Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
			if (dbComment == null) {
				throw new CustomizeException((CustomizeErrorCode.COMMENT_NOT_FOUND));
			}

			commentMapper.insert(comment);
		} else {
			//回复问题
			Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
			if (question == null) {
				throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
			}
			commentMapper.insert(comment);
			//评论数进行累加
			question.setCommentCount(1);
			questionExtMapper.incComment(question);
		}
	}

	public List<CommentDTO> listQuestionId(Long id) {
		CommentExample commentExample = new CommentExample();
		commentExample.createCriteria()
				.andParentIdEqualTo(id)
				.andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
		//将回复内容按照时间方式倒序，让新回复的内容出现在界面的最上面
		commentExample.setOrderByClause("gmt_creator desc");
		List<Comment> comments = commentMapper.selectByExample(commentExample);

		if (comments.size() == 0) {
			return new ArrayList<>();
		}
		//java8语法新特性，抽出所有commentators出来;
		//使用Lambda获取去重的评论人
		Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
		List<Long> userIds = new ArrayList();
		userIds.addAll(commentators);

		//获取评论人并转换为Map
		UserExample userExample = new UserExample();
		userExample.createCriteria()
				.andIdIn(userIds);
		List<User> users = userMapper.selectByExample(userExample);
		//将userList制作成为userMap
		Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

		//转换comment为commentDTO
		List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
			CommentDTO commentDTO = new CommentDTO();
			//属性复制
			BeanUtils.copyProperties(comment,commentDTO);
			commentDTO.setUser(userMap.get(comment.getCommentator()));

			return commentDTO;
		}).collect(Collectors.toList());

		return commentDTOs;
	}
}
