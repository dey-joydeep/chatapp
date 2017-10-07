package com.joy.ca.db.dao;

import java.util.List;

import com.joy.ca.db.entity.view.UserInfoView;

public interface UserInfoDAO {

	public UserInfoView getUserInfo(long loginId);

	public List<UserInfoView> getFriendList(long loginId);

	public String getSenderName(long loginId);
}
