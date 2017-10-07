package com.joy.ca.db.entity.table;

import java.util.Date;

public class LoginSessionEntity {

	private String sessionId;
	private long loginId;
	private Date sessionExpiryDate;
	private String deviceInfo;
	private String ipAddress;
	private Date lastAccessedAt;

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

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
	 * @return the sessionExpiryDate
	 */
	public Date getSessionExpiryDate() {
		return sessionExpiryDate;
	}

	/**
	 * @param sessionExpiryDate
	 *            the sessionExpiryDate to set
	 */
	public void setSessionExpiryDate(Date sessionExpiryDate) {
		this.sessionExpiryDate = sessionExpiryDate;
	}

	/**
	 * @return the deviceInfo
	 */
	public String getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * @param deviceInfo
	 *            the deviceInfo to set
	 */
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the lastAccessedAt
	 */
	public Date getLastAccessedAt() {
		return lastAccessedAt;
	}

	/**
	 * @param lastAccessedAt
	 *            the lastAccessedAt to set
	 */
	public void setLastAccessedAt(Date lastAccessedAt) {
		this.lastAccessedAt = lastAccessedAt;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LoginSessionEntity [" + (sessionId != null ? "sessionId=" + sessionId + ", " : "") + "loginId="
				+ loginId + ", " + (sessionExpiryDate != null ? "sessionExpiryDate=" + sessionExpiryDate + ", " : "")
				+ (deviceInfo != null ? "deviceInfo=" + deviceInfo + ", " : "")
				+ (ipAddress != null ? "ipAddress=" + ipAddress + ", " : "")
				+ (lastAccessedAt != null ? "lastAccessedAt=" + lastAccessedAt : "") + "]";
	}
}
