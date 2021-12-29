package com.wy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信用户信息
 * 
 * @author 飞花梦影
 * @date 2021-12-29 10:31:33
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenIdClass implements Serializable {

	private static final long serialVersionUID = -3694360969813383916L;

	private String access_token;

	private String expires_in;

	private String refresh_token;

	private String openid;

	private String scope;

	private String unionid;

	private String errcode;

	private String errmsg;
}