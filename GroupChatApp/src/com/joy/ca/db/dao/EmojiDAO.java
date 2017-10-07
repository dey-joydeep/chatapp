package com.joy.ca.db.dao;

import java.util.List;

import com.joy.ca.db.entity.table.EmojiEntity;

public interface EmojiDAO {

	void insertEmoji(EmojiEntity entity);

	List<EmojiEntity> selectAllEmoji();

	List<EmojiEntity> selectEmojiByGroup(int groupId);
}
