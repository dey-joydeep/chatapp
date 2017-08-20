package org.joy.ca.db.service;

import java.util.List;

import org.joy.ca.db.entity.table.EmojiEntity;

public interface EmojiGroupService {

	List<EmojiEntity> selectAllEmojiGroup();
}
