package com.wy.shiro.mapper;

import java.util.List;
import java.util.Map;

import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.Role;

/**
 * 用户适配器Mapper
 * 
 * @param
 * @return
 */
public interface UserAdapterMapper {

	/**
	 * 按用户Id查找对应角色
	 * 
	 * @param
	 * @return
	 */
	public List<Role> findRoleByUserId(Map<String, Object> map);

	/**
	 * 按用户Id查找对应资源
	 * 
	 * @param
	 * @return
	 */
	public List<Resource> findResourceByUserId(Map<String, Object> map);
}