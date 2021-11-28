package com.wy.service.inter;

import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.model.User;
import com.wy.service.UserService;

/**
 * 用户业务实现类
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:12:09
 * @git {@link https://github.com/mygodness100}
 */
@Service
public class UserServiceImpl extends AbstractService<User,Long> implements UserService {

	@Override
	public boolean checkUsername(String username) {
		return false;
	}

	@Override
	public User login(String username, String password) {
		return null;
	}
}