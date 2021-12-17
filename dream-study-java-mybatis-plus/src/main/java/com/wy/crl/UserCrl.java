package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.User;
import com.wy.base.AbstractCrl;
import com.wy.result.Result;
import com.wy.service.UserService;

import io.swagger.annotations.Api;

/**
 * User用户API
 * 
 * @auther 飞花梦影
 * @date 2021-05-05 14:48:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "User用户API")
@RestController
@RequestMapping("user")
public class UserCrl extends AbstractCrl<User, Long> {

	@Autowired
	private UserService userService;

	@PostMapping("resetPwd")
	public Result<?> resetPwd(@RequestBody User user) {
		return Result.ok(userService.resetPwd(user));
	}
}