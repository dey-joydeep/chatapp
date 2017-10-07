package com.joy.ca.db.service;

import java.util.List;

import com.joy.ca.db.entity.table.EmojiEntity;

public interface EmojiGroupService {

	List<EmojiEntity> selectAllEmojiGroup();
}
