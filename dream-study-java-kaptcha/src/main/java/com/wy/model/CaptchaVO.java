package com.wy.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-16 17:44:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class CaptchaVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 验证码标识符
	 */
	private String captchaKey;

	/**
	 * 验证码过期时间
	 */
	private Long expire;

	/**
	 * base64字符串
	 */
	private String base64Img;
}