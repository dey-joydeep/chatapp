package org.joy.ca.db.dao;

import java.util.List;

import org.joy.ca.db.entity.table.EmojiEntity;

public interface EmojiGroupDAO {

	List<EmojiEntity> selectAllEmojiGroup();
}
