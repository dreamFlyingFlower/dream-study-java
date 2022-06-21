package com.wy.shiro.core.bridge;

import java.util.List;

import org.apache.shiro.authz.AuthorizationInfo;

import com.wy.shiro.core.base.ShiroUser;
import com.wy.shiro.entity.User;

/**
 * 用户信息桥接（后期会做缓存）
 */
public interface UserBridgeService {

	/**
	 * @Description 查找用户信息
	 * @param loginName 用户名称
	 * @return user对象
	 */
	User findUserByLoginName(String loginName);

	/**
	 * @Description 鉴权方法
	 * @param shiroUser 令牌对象
	 * @return 鉴权信息
	 */
	AuthorizationInfo getAuthorizationInfo(ShiroUser shiroUser);

	/**
	 * @Description 查询用户对应角色标识list
	 * @param userId 用户id
	 * @return 角色标识集合
	 */
	List<String> findRoleList(String key, String userId);

	/**
	 * @Description 查询用户对应资源标识list
	 * @param userId 用户id
	 * @return 资源标识集合
	 */
	List<String> findResourcesList(String key, String userId);

	/**
	 * @Description 查询资源ids
	 * @param userId 用户id
	 * @return 资源id集合
	 */
	List<String> findResourcesIds(String userId);

	/**
	 * @Description 登录成后加载缓存信息
	 * @param shiroUser 令牌对象
	 * @return
	 */
	void loadUserAuthorityToCache(ShiroUser shiroUser);
}
