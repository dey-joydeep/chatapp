package org.joy.ca.db.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.joy.ca.db.config.SessionConfig;
import org.joy.ca.db.dao.EmojiGroupDAO;
import org.joy.ca.db.entity.table.EmojiEntity;

public class EmojiGroupServiceImpl implements EmojiGroupService {

	@Override
	public List<EmojiEntity> selectAllEmojiGroup() {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			EmojiGroupDAO emojiGroupDAO = sqlSession.getMapper(EmojiGroupDAO.class);
			return emojiGroupDAO.selectAllEmojiGroup();
		}
	}
}
