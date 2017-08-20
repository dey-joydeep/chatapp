package org.joy.ca.db.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.joy.ca.db.config.SessionConfig;
import org.joy.ca.db.dao.EmojiDAO;
import org.joy.ca.db.entity.table.EmojiEntity;

public class EmojiServiceImpl implements EmojiService {

	@Override
	public void insertEmoji(EmojiEntity entity) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			EmojiDAO emojiDAO = sqlSession.getMapper(EmojiDAO.class);
			emojiDAO.insertEmoji(entity);
			sqlSession.commit();
		}
	}

	@Override
	public List<EmojiEntity> getAllEmoji() {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			EmojiDAO emojiDAO = sqlSession.getMapper(EmojiDAO.class);
			return emojiDAO.selectAllEmoji();
		}
	}

	@Override
	public List<EmojiEntity> selectEmojiByGroup(int groupId) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			EmojiDAO emojiDAO = sqlSession.getMapper(EmojiDAO.class);
			return emojiDAO.selectEmojiByGroup(groupId);
		}
	}

}
