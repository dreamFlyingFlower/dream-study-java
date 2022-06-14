package com.wy.service;

import com.wy.base.BaseService;
import com.wy.model.User;

/**
 * User用户业务接口
 * 
 * @author 飞花梦影
 * @date 2021-04-08 13:55:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface UserService extends BaseService<User, Long> {

	Object resetPwd(User user);
	
	void test1();
	
	void test2();
}