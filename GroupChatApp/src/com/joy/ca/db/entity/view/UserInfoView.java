package com.joy.ca.db.entity.view;

import java.util.Date;

public class UserInfoView {

	private Long loginId;
	private String email;
	private String username;
	private String fullname;
	private boolean active;
	private Date lastLoginAt;

	/**
	 * @return the loginId
	 */
	public Long getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId
	 *            the loginId to set
	 */
	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}

	/**
	 * @param fullname
	 *            the fullname to set
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the lastLoginAt
	 */
	public Date getLastLoginAt() {
		return lastLoginAt;
	}

	/**
	 * @param lastLoginAt
	 *            the lastLoginAt to set
	 */
	public void setLastLoginAt(Date lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserInfoView [loginId=" + loginId + ", " + (email != null ? "email=" + email + ", " : "")
				+ (username != null ? "username=" + username + ", " : "")
				+ (fullname != null ? "fullname=" + fullname + ", " : "") + "active=" + active + ", "
				+ (lastLoginAt != null ? "lastLoginAt=" + lastLoginAt : "") + "]";
	}
}
