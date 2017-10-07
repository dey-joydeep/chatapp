package com.joy.ca.db.service;

import com.joy.ca.db.entity.table.LoginEntity;

public interface LoginService {
	public LoginEntity getAuthenticationData(Object userId);

	public void updateLastLogin(long loginId);

	public void updateLastOnline(long loginId);
}
