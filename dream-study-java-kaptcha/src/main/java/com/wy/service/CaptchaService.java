package com.wy.service;

import com.wy.entity.CaptchaVO;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-16 17:44:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface CaptchaService {

	CaptchaVO cacheCaptcha(String captcha);
}