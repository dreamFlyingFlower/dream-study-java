package com.wy.shiro.core.adapter;

import java.util.List;

import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.Role;
import com.wy.shiro.entity.User;

/**
 * 后台登陆用户适配器接口
 * 
 * @author 飞花梦影
 * @date 2022-06-22 11:26:47
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface UserAdapter {

	/**
	 * 按用户名查找用户
	 * 
	 * @param loginName 登录名
	 * @return
	 */
	User findUserByLoginName(String loginName);

	/**
	 * 查找用户所有角色
	 * 
	 * @param userId 用户Id
	 * @return
	 */
	List<Role> findRoleByUserId(String userId);

	/**
	 * 查询用户有那些资源
	 * 
	 * @param userId 用户Id
	 * @return
	 */
	List<Resource> findResourceByUserId(String userId);
}