package com.wy.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试登录
 *
 * @author 飞花梦影
 * @date 2025-09-02 16:47:28
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
public class LoginController {

	@GetMapping("/login")
	@LimitCount(key = "login", name = "登录接口", prefix = "limit")
	public String login(@RequestParam(required = true) String username, @RequestParam(required = true) String password,
			HttpServletRequest request) throws Exception {
		if (StringUtils.equals("张三", username) && StringUtils.equals("123456", password)) {
			return "登录成功";
		}
		return "账户名或密码错误";
	}
}