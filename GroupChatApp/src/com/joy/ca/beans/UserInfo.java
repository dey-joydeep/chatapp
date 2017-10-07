package com.joy.ca.beans;

/**
 * Holds the basic details of logged in users
 *
 * @author Joydeep Dey
 *
 */
public class UserInfo {

	private long loginId;
	private String realName;

	/**
	 * @return the loginId
	 */
	public long getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId
	 *            the loginId to set
	 */
	public void setLoginId(long loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserInfo [loginId=" + loginId + ", " + (realName != null ? "realName=" + realName : "") + "]";
	}
}
