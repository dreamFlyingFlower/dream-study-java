package com.wy.service;

import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.model.User;
import com.wy.service.inter.IUserService;

/**
 * 用户业务实现类
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:12:09
 * @git {@link https://github.com/mygodness100}
 */
@Service
public class UserService extends AbstractService<User,Long> implements IUserService {

	@Override
	public boolean checkUsername(String username) {
		return false;
	}

	@Override
	public User login(String username, String password) {
		return null;
	}
}