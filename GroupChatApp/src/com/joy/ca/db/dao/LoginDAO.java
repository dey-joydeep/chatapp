package com.joy.ca.db.dao;

import com.joy.ca.db.entity.table.LoginEntity;

public interface LoginDAO {
	public LoginEntity getAuthenticationData(Object userId);

	public void updateLastLogin(long loginId);

	public void updateLastOnline(long loginId);
}
