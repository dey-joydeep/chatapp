package com.joy.ca.db.dao;

import java.util.Date;

import com.joy.ca.db.entity.table.LoginSessionEntity;

public interface LoginSessionDAO {

	public Date verifyLoginSessionAndGetExpiryDate(LoginSessionEntity entity);

	public void deleteLoginSession(LoginSessionEntity entity);

	public void insertLoginSession(LoginSessionEntity entity);
}
