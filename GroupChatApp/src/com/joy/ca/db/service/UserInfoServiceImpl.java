package com.joy.ca.db.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.joy.ca.db.config.SessionConfig;
import com.joy.ca.db.dao.UserInfoDAO;
import com.joy.ca.db.entity.view.UserInfoView;

public class UserInfoServiceImpl implements UserInfoService {

	@Override
	public UserInfoView getUserInfoByLoginId(long loginId) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			UserInfoDAO userInfoDAO = sqlSession.getMapper(UserInfoDAO.class);
			return userInfoDAO.getUserInfo(loginId);
		}
	}

	@Override
	public List<UserInfoView> getFriendList(long loginId) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			UserInfoDAO userInfoDAO = sqlSession.getMapper(UserInfoDAO.class);
			return userInfoDAO.getFriendList(loginId);
		}
	}

	@Override
	public String getSenderName(long loginId) {
		try (SqlSession sqlSession = SessionConfig.getSession()) {
			UserInfoDAO userInfoDAO = sqlSession.getMapper(UserInfoDAO.class);
			return userInfoDAO.getSenderName(loginId);
		}
	}
}
