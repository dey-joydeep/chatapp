package org.joy.ca.db.entity.table;

import java.util.Date;

public class EmojiEntity {

	private int emojiId;
	private String emojiCode;
	private String emojiName;
	private byte[] emojiData;
	private int eGroupId;
	private boolean active;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

	public int getEmojiId() {
		return emojiId;
	}

	public void setEmojiId(int emojiId) {
		this.emojiId = emojiId;
	}

	public String getEmojiCode() {
		return emojiCode;
	}

	public void setEmojiCode(String emojiCode) {
		this.emojiCode = emojiCode;
	}

	public String getEmojiName() {
		return emojiName;
	}

	public void setEmojiName(String emojiName) {
		this.emojiName = emojiName;
	}

	public byte[] getEmojiData() {
		return emojiData;
	}

	public void setEmojiData(byte[] emojiData) {
		this.emojiData = emojiData;
	}

	public int geteGroupId() {
		return eGroupId;
	}

	public void seteGroupId(int eGroupId) {
		this.eGroupId = eGroupId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
}
