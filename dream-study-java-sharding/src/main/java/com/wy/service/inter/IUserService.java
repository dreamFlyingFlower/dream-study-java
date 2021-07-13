package com.wy.service.inter;

import com.wy.base.BaseService;
import com.wy.model.User;

/**
 * 用户业务接口
 * 
 * @author ParadiseWY
 * @date 2020-11-23 10:13:06
 * @git {@link https://github.com/mygodness100}
 */
public interface IUserService extends BaseService<User,Long> {

	boolean checkUsername(String username);

	User login(String username, String password);
}