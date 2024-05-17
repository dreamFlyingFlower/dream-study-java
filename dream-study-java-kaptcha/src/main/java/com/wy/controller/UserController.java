package com.wy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dream.result.Result;
import com.wy.entity.LoginDTO;

import dream.flying.flower.autoconfigure.web.helper.RedisHelpers;
import dream.framework.web.controller.BaseController;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-16 17:46:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("/user")
public class UserController implements BaseController {

	@Autowired
	private RedisHelpers redisHelpers;

	@PostMapping("/login")
	public Result<?> login(@RequestBody LoginDTO loginDTO) {
		Object captch = redisHelpers.get(loginDTO.getCaptchaKey());
		if (captch == null) {
			// throw 验证码已过期
		}
		if (!loginDTO.getCaptcha().equals(captch)) {
			// throw 验证码错误
		}
		// 查询用户信息

		// 判断用户是否存在 不存在抛出用户名密码错误

		// 判断密码是否正确，不正确抛出用户名密码错误

		// 构造返回到前端的用户对象并封装信息和生成token

		return ok(captch);
	}
}