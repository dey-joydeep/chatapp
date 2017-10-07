package com.joy.ca.db.service;

import java.util.List;

import com.joy.ca.db.entity.view.MessageInfoView;

public interface MessageInfoService {

	List<MessageInfoView> getAllmessages();

	MessageInfoView getMessageById(long messageId);
}
