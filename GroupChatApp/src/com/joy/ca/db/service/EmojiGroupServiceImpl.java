package com.joy.ca.db.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.joy.ca.db.config.SessionConfig;
import com.joy.ca.db.dao.EmojiGroupDAO;
import com.joy.ca.db.entity.table.EmojiEntity;

public class EmojiGroupServiceImpl implements EmojiGroupService {

	@Override
	public List<EmojiEntity> selectAllEmojiGroup() {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			EmojiGroupDAO emojiGroupDAO = sqlSession.getMapper(EmojiGroupDAO.class);
			return emojiGroupDAO.selectAllEmojiGroup();
		}
	}
}
