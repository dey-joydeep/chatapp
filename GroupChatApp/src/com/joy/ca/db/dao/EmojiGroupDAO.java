package com.joy.ca.db.dao;

import java.util.List;

import com.joy.ca.db.entity.table.EmojiEntity;

public interface EmojiGroupDAO {

	List<EmojiEntity> selectAllEmojiGroup();
}
