package org.joy.ca.db.dao;

import java.util.List;

import org.joy.ca.db.entity.table.EmojiEntity;

public interface EmojiDAO {

	void insertEmoji(EmojiEntity entity);

	List<EmojiEntity> selectAllEmoji();

	List<EmojiEntity> selectEmojiByGroup(int groupId);
}
