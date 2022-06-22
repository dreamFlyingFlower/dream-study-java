package com.wy.shiro.core.adapter.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wy.shiro.constant.SuperConstant;
import com.wy.shiro.core.adapter.UserAdapter;
import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.Role;
import com.wy.shiro.entity.User;
import com.wy.shiro.entity.UserExample;
import com.wy.shiro.mapper.UserAdapterMapper;
import com.wy.shiro.mapper.UserMapper;

/**
 * 后台登陆用户适配器接口实现
 * 
 * @author 飞花梦影
 * @date 2022-06-22 11:27:13
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
public class UserAdapterImpl implements UserAdapter {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserAdapterMapper userAdapterMapper;

	@Override
	public User findUserByLoginName(String loginName) {
		UserExample userExample = new UserExample();
		userExample.createCriteria().andEnableFlagEqualTo(SuperConstant.YES).andLoginNameEqualTo(loginName);
		List<User> userList = userMapper.selectByExample(userExample);
		if (userList.size() == 1) {
			return userList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Role> findRoleByUserId(String userId) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("userId", userId);
		values.put("enableFlag", SuperConstant.YES);
		List<Role> list = userAdapterMapper.findRoleByUserId(values);
		return list;
	}

	@Override
	public List<Resource> findResourceByUserId(String userId) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("userId", userId);
		values.put("enableFlag", SuperConstant.YES);
		List<Resource> list = userAdapterMapper.findResourceByUserId(values);
		return list;
	}
}