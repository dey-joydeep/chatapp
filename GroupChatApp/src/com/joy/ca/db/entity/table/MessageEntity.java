package com.joy.ca.db.entity.table;

import java.util.Date;

public class MessageEntity {
	private long messageId;
	private long senderId;
	private Integer receiverId;
	private String message;
	private Date sentAt;
	private String senderIp;

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getSentAt() {
		return sentAt;
	}

	public void setSentAt(Date sentAt) {
		this.sentAt = sentAt;
	}

	public String getSenderIp() {
		return senderIp;
	}

	public void setSenderIp(String senderIp) {
		this.senderIp = senderIp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageEntity [messageId=" + messageId + ", senderId=" + senderId + ", "
				+ (receiverId != null ? "receiverId=" + receiverId + ", " : "")
				+ (message != null ? "message=" + message + ", " : "")
				+ (sentAt != null ? "sentAt=" + sentAt + ", " : "") + (senderIp != null ? "senderIp=" + senderIp : "")
				+ "]";
	}
}
