package com.joy.ca.db.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.joy.ca.db.config.SessionConfig;
import com.joy.ca.db.dao.MessageInfoDAO;
import com.joy.ca.db.entity.view.MessageInfoView;

public class MessageInfoServiceImpl implements MessageInfoService {

	@Override
	public List<MessageInfoView> getAllmessages() {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			MessageInfoDAO messageInfoDAO = sqlSession.getMapper(MessageInfoDAO.class);
			return messageInfoDAO.selectAllmessages();
		}
	}

	@Override
	public MessageInfoView getMessageById(long messageId) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			MessageInfoDAO messageInfoDAO = sqlSession.getMapper(MessageInfoDAO.class);
			return messageInfoDAO.selectMessageById(messageId);
		}
	}
}
