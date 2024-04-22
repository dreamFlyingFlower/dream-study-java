package com.wy.mybatis.atomikos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试事务异常回滚
 *
 * @author 飞花梦影
 * @date 2024-04-22 17:06:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class UserInfoService2 {

	@Autowired
	private UserInfoMapper2 userInfoMapper2;

	@Transactional
	public int insert(String userId, String username, Integer age) {
		int insert = userInfoMapper2.insert(userId, username, age);
		int i = 1 / 0;
		System.out.println(i);
		return insert;
	}
}