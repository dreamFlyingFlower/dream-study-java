package com.wy.convert;

import com.wy.model.User;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2025-12-29 16:50:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public abstract class UserDecorate implements UserConvert {

	private UserConvert userConvert;

	public UserDecorate(UserConvert userConvert) {
		this.userConvert = userConvert;
	}

	@Override
	public User toUser(Object object) {
		// DO SOMETHING
		userConvert.toUser(object);
		// DO SOMETHING
		return null;
	}
}