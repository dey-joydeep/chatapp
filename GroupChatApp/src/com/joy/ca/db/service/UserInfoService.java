package com.joy.ca.db.service;

import java.util.List;

import com.joy.ca.db.entity.view.UserInfoView;

public interface UserInfoService {

	UserInfoView getUserInfoByLoginId(long loginId);

	List<UserInfoView> getFriendList(long loginId);

	String getSenderName(long loginId);
}
