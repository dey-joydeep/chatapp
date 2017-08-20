package org.joy.ca.db.service;

import java.util.List;
import java.util.Map;

import org.joy.ca.db.entity.view.UserInfoView;

public interface UserInfoService {

	Integer authenticateUser(Map<String, String> paramValMap);

	UserInfoView getUserInfoByLoginId(int loginId);

	List<UserInfoView> getFriendList(int loginId);
}
