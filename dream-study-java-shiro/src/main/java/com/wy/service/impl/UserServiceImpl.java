package com.wy.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.env.DefaultEnvironment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.wy.model.User;
import com.wy.service.UserService;

/**
 * User用户业务处理
 *
 * @author 飞花梦影
 * @date 2021-04-17 17:40:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class UserServiceImpl implements UserService {

	/**
	 * 通过用户名从数据库中取用户信息,用户名必须唯一
	 *
	 * @param username 用户名
	 * @return User
	 */
	public User getByUsername(String username) {
		System.out.println(username);
		DefaultEnvironment environment = new DefaultEnvironment();
		// 获取securityManager实例
		SecurityManager securityManager = environment.getSecurityManager();
		// 把securityManager实例绑定到SecurityUtils
		SecurityUtils.setSecurityManager(securityManager);
		// 得到当前执行的用户
		Subject currentUser = SecurityUtils.getSubject();
		// 创建token令牌,用户名/密码
		UsernamePasswordToken token = new UsernamePasswordToken("java1234", "12345");
		try {
			// 身份认证
			currentUser.login(token);
			System.out.println("身份认证成功！");
			currentUser.hasRole("admin");
			currentUser.isPermitted("权限");
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		// 退出
		currentUser.logout();
		return null;
	}
}