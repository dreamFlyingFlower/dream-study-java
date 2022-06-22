package com.wy.shiro.core;

import org.apache.shiro.authc.UsernamePasswordToken;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义token
 * 
 * @author 飞花梦影
 * @date 2022-06-22 16:25:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
public class SimpleToken extends UsernamePasswordToken {

	private static final long serialVersionUID = -4849823851197352099L;

	private String tokenType;

	private String quickPassword;

	public SimpleToken(String tokenType, String username, String password) {
		super(username, password);
		this.tokenType = tokenType;
	}

	public SimpleToken(String tokenType, String username, String password, String quickPassword) {
		super(username, password);
		this.tokenType = tokenType;
		this.quickPassword = quickPassword;
	}
}