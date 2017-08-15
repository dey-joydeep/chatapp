package org.joy.ca.beans;

public class MessageBean {

	private String senderName;
	private String senderIp;
	private String message;
	private String sentTimestamp;
	private String senderPcName;
	private String username;

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderIp() {
		return senderIp;
	}

	public void setSenderIp(String senderIp) {
		this.senderIp = senderIp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSentTimestamp() {
		return sentTimestamp;
	}

	public void setSentTimestamp(String sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}

	public String getSenderPcName() {
		return senderPcName;
	}

	public void setSenderPcName(String senderName) {
		this.senderPcName = senderName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
