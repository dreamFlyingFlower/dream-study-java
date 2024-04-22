package com.wy.mybatis.atomikos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-22 17:54:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
public class MyBatisController {

	@Autowired
	private UserInfoService1 userInfoService1;

	@Autowired
	private UserInfoService2 userInfoService2;

	@RequestMapping(value = "datasource1")
	public int datasource1(String userId, String username, Integer age) {
		return userInfoService1.insert(userId, username, age);
	}

	@RequestMapping(value = "datasource2")
	public int datasource2(String userId, String username, Integer age) {
		return userInfoService2.insert(userId, username, age);
	}

	@RequestMapping(value = "insertDb1AndDb2")
	public int insertDb1AndDb2(String userId, String username, Integer age) {
		return userInfoService1.insertDb1AndDb2(userId, username, age);
	}
}