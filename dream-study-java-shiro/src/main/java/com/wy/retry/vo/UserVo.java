package com.wy.retry.vo;

import com.wy.retry.pojo.User;

public class UserVo extends User {

	private static final long serialVersionUID = -3745165122177843152L;

	/**
	 * 当前拥有的角色Ids
	 **/
	private String hasRoleIds;

	/**
	 * 零时密码
	 **/
	private String plainPassword;

	public String getHasRoleIds() {
		return hasRoleIds;
	}

	public void setHasRoleIds(String hasRoleIds) {
		this.hasRoleIds = hasRoleIds == null ? null : hasRoleIds.trim();
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword == null ? null : plainPassword.trim();
	}
}