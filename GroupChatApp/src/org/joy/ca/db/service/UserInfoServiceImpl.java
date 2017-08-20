package org.joy.ca.db.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.joy.ca.db.config.SessionConfig;
import org.joy.ca.db.dao.UserInfoDAO;
import org.joy.ca.db.entity.view.UserInfoView;

public class UserInfoServiceImpl implements UserInfoService {

	@Override
	public Integer authenticateUser(Map<String, String> paramValMap) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			UserInfoDAO userInfoDAO = sqlSession.getMapper(UserInfoDAO.class);
			return userInfoDAO.authenticateUser(paramValMap);
		}
	}

	@Override
	public UserInfoView getUserInfoByLoginId(int loginId) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			UserInfoDAO userInfoDAO = sqlSession.getMapper(UserInfoDAO.class);
			return userInfoDAO.getUserInfo(loginId);
		}
	}

	@Override
	public List<UserInfoView> getFriendList(int loginId) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			UserInfoDAO userInfoDAO = sqlSession.getMapper(UserInfoDAO.class);
			return userInfoDAO.getFriendList(loginId);
		}
	}
}
