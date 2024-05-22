package com.wy.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wy.entity.CaptchaVO;
import com.wy.service.CaptchaService;

import dream.flying.flower.autoconfigure.web.helper.RedisHelpers;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-16 17:45:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

	@Value("${server.session.timeout:1}")
	private Long timeout;

	@Autowired
	private RedisHelpers redisHelpers;

	private final String CAPTCHA_KEY = "captcha:verification:";

	@Override
	public CaptchaVO cacheCaptcha(String captcha) {
		String captchaKey = UUID.randomUUID().toString();
		redisHelpers.setExpire(CAPTCHA_KEY.concat(captchaKey), captcha, timeout);
		CaptchaVO captchaVO = new CaptchaVO();
		captchaVO.setCaptchaKey(captchaKey);
		captchaVO.setExpire(timeout);
		return captchaVO;
	}
}