package com.wy.service;

import com.wy.base.BaseService;
import com.wy.model.User;

public interface UserService extends BaseService<User, Long> {

	void getUserBySpecification();
}