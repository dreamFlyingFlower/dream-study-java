package com.wy.shiro.mapper;

import java.util.List;
import java.util.Map;

import com.wy.shiro.entity.Role;

/**
 * 角色服务层mapper
 * 
 * @param
 * @return
 */
public interface RoleServiceMapper {

	/**
	 * 查询角色拥有的资源Id字符串
	 * 
	 * @param
	 * @return
	 */
	List<String> findRoleHasResourceIds(Map<String, Object> map);

	/**
	 * 查询任务角色
	 * 
	 * @param
	 * @return
	 */
	List<Role> findRoleDetailByLenderId(Map<String, Object> map);
}