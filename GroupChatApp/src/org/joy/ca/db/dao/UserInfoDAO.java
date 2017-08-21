package org.joy.ca.db.dao;

import java.util.List;
import java.util.Map;

import org.joy.ca.db.entity.view.UserInfoView;

public interface UserInfoDAO {

	Integer authenticateUser(Map<String, String> params);

	UserInfoView getUserInfo(int loginId);

	List<UserInfoView> getFriendList(int loginId);
}
