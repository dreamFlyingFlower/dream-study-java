package com.wy.shiro.core.bridge.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wy.collection.ListTool;
import com.wy.shiro.constant.CacheConstant;
import com.wy.shiro.core.SimpleCacheService;
import com.wy.shiro.core.adapter.UserAdapter;
import com.wy.shiro.core.base.ShiroUser;
import com.wy.shiro.core.base.SimpleMapCache;
import com.wy.shiro.core.bridge.UserBridgeService;
import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.Role;
import com.wy.shiro.entity.User;
import com.wy.shiro.utils.ShiroUtil;

/**
 * 用户信息桥接（后期会做缓存）
 */
@Component("userBridgeService")
public class UserBridgeServiceImpl implements UserBridgeService {

	@Autowired
	UserAdapter userAdapter;

	@Autowired
	SimpleCacheService simpleCacheService;

	@Override
	public User findUserByLoginName(String loginName) {
		String key = CacheConstant.FIND_USER_BY_LOGINNAME + loginName;
		// 获取缓存
		Cache<Object, Object> cache = simpleCacheService.getCache(key);
		// 缓存存在
		if (Objects.nonNull(cache)) {
			return (User) cache.get(key);
		}
		// 缓存不存在
		User user = userAdapter.findUserByLoginName(loginName);
		if (Objects.nonNull(user)) {
			Map<Object, Object> map = new HashMap<>();
			map.put(key, user);
			SimpleMapCache simpleMapCache = new SimpleMapCache(key, map);
			simpleCacheService.creatCache(key, simpleMapCache);
		}
		return user;
	}

	@Override
	public List<String> findResourcesIds(String userId) {
		String sessionId = ShiroUtil.getShiroSessionId();
		String key = CacheConstant.RESOURCES_KEY_IDS + sessionId;
		List<Resource> resources = new ArrayList<>();
		// 获取缓存
		Cache<Object, Object> cache = simpleCacheService.getCache(key);
		// 缓存存在
		if (Objects.nonNull(cache)) {
			resources = (List<Resource>) cache.get(key);
		} else {
			// 缓存不存在
			resources = userAdapter.findResourceByUserId(userId);
			if (ListTool.isNotEmpty(resources)) {
				Map<Object, Object> map = new HashMap<>();
				map.put(key, resources);
				SimpleMapCache simpleMapCache = new SimpleMapCache(key, map);
				simpleCacheService.creatCache(key, simpleMapCache);
			}

		}

		List<String> ids = new ArrayList<>();
		for (Resource resource : resources) {
			ids.add(resource.getId());
		}
		return ids;
	}

	@Override
	public AuthorizationInfo getAuthorizationInfo(ShiroUser shiroUser) {
		String sessionId = ShiroUtil.getShiroSessionId();
		String roleKey = CacheConstant.ROLE_KEY + sessionId;
		String resourcesKey = CacheConstant.RESOURCES_KEY + sessionId;
		// 查询用户对应的角色标识
		List<String> roleList = this.findRoleList(roleKey, shiroUser.getId());
		// 查询用户对于的资源标识
		List<String> resourcesList = this.findResourcesList(resourcesKey, shiroUser.getId());
		// 构建鉴权信息对象
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addRoles(roleList);
		simpleAuthorizationInfo.addStringPermissions(resourcesList);
		return simpleAuthorizationInfo;
	}

	@Override
	public List<String> findRoleList(String key, String userId) {
		List<Role> roles = new ArrayList<>();
		// 获得缓存
		Cache<Object, Object> cache = simpleCacheService.getCache(key);
		// 缓存存在
		if (Objects.nonNull(cache)) {
			roles = (List<Role>) cache.get(key);
		} else {
			// 缓存不存在
			roles = userAdapter.findRoleByUserId(userId);
			if (ListTool.isNotEmpty(roles)) {
				Map<Object, Object> map = new HashMap<>();
				map.put(key, roles);
				SimpleMapCache simpleMapCache = new SimpleMapCache(key, map);
				simpleCacheService.creatCache(key, simpleMapCache);
			}
		}

		List<String> roleLabel = new ArrayList<>();
		for (Role role : roles) {
			roleLabel.add(role.getLabel());
		}
		return roleLabel;
	}

	@Override
	public List<String> findResourcesList(String key, String userId) {
		List<Resource> resources = new ArrayList<>();
		// 获得缓存
		Cache<Object, Object> cache = simpleCacheService.getCache(key);
		// 缓存存在
		if (Objects.nonNull(cache)) {
			resources = (List<Resource>) cache.get(key);
		} else {
			// 缓存不存在
			resources = userAdapter.findResourceByUserId(userId);
			if (ListTool.isNotEmpty(resources)) {
				Map<Object, Object> map = new HashMap<>();
				map.put(key, resources);
				SimpleMapCache simpleMapCache = new SimpleMapCache(key, map);
				simpleCacheService.creatCache(key, simpleMapCache);
			}
		}
		List<String> resourceLabel = new ArrayList<>();
		for (Resource resource : resources) {
			resourceLabel.add(resource.getLabel());
		}
		return resourceLabel;
	}

	@Override
	public void loadUserAuthorityToCache(ShiroUser shiroUser) {
		String sessionId = ShiroUtil.getShiroSessionId();
		String roleKey = CacheConstant.ROLE_KEY + sessionId;
		String resourcesKey = CacheConstant.RESOURCES_KEY + sessionId;
		// 查询用户对应的角色标识
		List<String> roleList = this.findRoleList(roleKey, shiroUser.getId());
		// 查询用户对于的资源标识
		List<String> resourcesList = this.findResourcesList(resourcesKey, shiroUser.getId());
	}
}