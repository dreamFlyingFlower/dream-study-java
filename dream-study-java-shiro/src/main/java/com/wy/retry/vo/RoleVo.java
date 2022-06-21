package com.wy.retry.vo;

import com.wy.retry.pojo.Role;

/**
 * 角色vo
 */
public class RoleVo extends Role {

	/** serialVersionUID */
	private static final long serialVersionUID = -8242413497560284592L;

	/** 是否拥有资源 **/
	private String hasResourceIds;

	public String getHasResourceIds() {
		return hasResourceIds;
	}

	public void setHasResourceIds(String hasResourceIds) {
		this.hasResourceIds = hasResourceIds == null ? null : hasResourceIds.trim();
	}
}