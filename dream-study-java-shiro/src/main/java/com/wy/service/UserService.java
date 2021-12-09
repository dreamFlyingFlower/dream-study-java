package com.wy.service;

import com.wy.model.User;

/**
 * User用户业务接口
 *
 * @author 飞花梦影
 * @date 2021-04-17 17:40:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface UserService {

	/**
	 * 通过用户名从数据库中取用户信息,用户名必须唯一
	 *
	 * @param username 用户名
	 * @return User
	 */
	User getByUsername(String username);
}