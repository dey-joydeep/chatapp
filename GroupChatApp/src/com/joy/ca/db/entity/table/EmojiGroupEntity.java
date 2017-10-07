package com.joy.ca.db.entity.table;

import java.util.Arrays;
import java.util.Date;

public class EmojiGroupEntity {

	private int eGroupId;
	private String eGroupName;
	private byte[] eGroupIcon;
	private boolean active;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

	public int geteGroupId() {
		return eGroupId;
	}

	public void seteGroupId(int eGroupId) {
		this.eGroupId = eGroupId;
	}

	public String geteGroupName() {
		return eGroupName;
	}

	public void seteGroupName(String eGroupName) {
		this.eGroupName = eGroupName;
	}

	public byte[] geteGroupIcon() {
		return eGroupIcon;
	}

	public void seteGroupIcon(byte[] eGroupIcon) {
		this.eGroupIcon = eGroupIcon;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmojiGroupEntity [eGroupId=" + eGroupId + ", "
				+ (eGroupName != null ? "eGroupName=" + eGroupName + ", " : "")
				+ (eGroupIcon != null ? "eGroupIcon=" + Arrays.toString(eGroupIcon) + ", " : "") + "active=" + active
				+ ", " + (createdAt != null ? "createdAt=" + createdAt + ", " : "")
				+ (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "")
				+ (deletedAt != null ? "deletedAt=" + deletedAt : "") + "]";
	}
}
