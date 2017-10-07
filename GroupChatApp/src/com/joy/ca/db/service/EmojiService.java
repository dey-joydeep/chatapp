package com.joy.ca.db.service;

import java.util.List;

import com.joy.ca.db.entity.table.EmojiEntity;

public interface EmojiService {

	void insertEmoji(EmojiEntity entity);

	List<EmojiEntity> getAllEmoji();

	List<EmojiEntity> selectEmojiByGroup(int groupId);
}
