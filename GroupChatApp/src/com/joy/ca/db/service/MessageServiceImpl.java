package com.joy.ca.db.service;

import org.apache.ibatis.session.SqlSession;

import com.joy.ca.db.config.SessionConfig;
import com.joy.ca.db.dao.MessageDAO;
import com.joy.ca.db.entity.table.MessageEntity;

public class MessageServiceImpl implements MessageService {

	@Override
	public void insertMessage(MessageEntity entity) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			MessageDAO messageDAO = sqlSession.getMapper(MessageDAO.class);
			messageDAO.insertMessage(entity);
			sqlSession.commit();
		}
	}
}
