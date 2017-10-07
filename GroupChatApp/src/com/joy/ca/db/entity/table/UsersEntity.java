package com.joy.ca.db.entity.table;

import java.util.Date;

public class UsersEntity {

	private int userId;
	private int loginId;
	private String username;
	private String firstname;
	private String lastname;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLoginId() {
		return loginId;
	}

	public void setLoginId(int loginId) {
		this.loginId = loginId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UsersEntity [userId=" + userId + ", loginId=" + loginId + ", "
				+ (username != null ? "username=" + username + ", " : "")
				+ (firstname != null ? "firstname=" + firstname + ", " : "")
				+ (lastname != null ? "lastname=" + lastname + ", " : "")
				+ (createdAt != null ? "createdAt=" + createdAt + ", " : "")
				+ (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "")
				+ (deletedAt != null ? "deletedAt=" + deletedAt : "") + "]";
	}
}
