package com.wy.redis;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义token数据结构
 *
 * @author 飞花梦影
 * @date 2022-06-09 10:55:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SelfShiroToken implements AuthenticationToken {

	private static final long serialVersionUID = 1L;

	/** 存储到redis中的token */
	private String token;

	public SelfShiroToken(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}