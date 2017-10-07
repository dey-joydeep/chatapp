package com.joy.ca.db.dao;

import java.util.List;

import com.joy.ca.db.entity.view.MessageInfoView;

public interface MessageInfoDAO {

	List<MessageInfoView> selectAllmessages();

	MessageInfoView selectMessageById(long messageId);
}
