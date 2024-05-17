package com.wy.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-16 17:46:28
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class LoginDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;

	private String pwd;

	private String captchaKey;

	private String captcha;
}