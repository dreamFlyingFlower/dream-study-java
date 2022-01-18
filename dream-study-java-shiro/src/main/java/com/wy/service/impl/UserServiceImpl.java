package com.wy.service.impl;

import java.util.Arrays;
import java.util.List;

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
		// 创建securityManager工厂,通过ini配置文件创建securityManager工厂
		// Factory<SecurityManager> factory = new
		// IniSecurityManagerFactory("classpath:shiro-realm.ini");
		DefaultEnvironment environment = new DefaultEnvironment();
		// 获取securityManager实例
		SecurityManager securityManager = environment.getSecurityManager();
		// 把securityManager实例绑定到SecurityUtils
		SecurityUtils.setSecurityManager(securityManager);
		// 得到当前执行的用户
		Subject currentUser = SecurityUtils.getSubject();
		// 创建token令牌,用户名/密码
		UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");
		try {
			// 身份认证
			currentUser.login(token);
			System.out.println("身份认证是否成功:" + currentUser.isAuthenticated());
			// 基于角色的授权
			boolean ishasRole = currentUser.hasRole("admin");
			System.out.println("单个角色判断" + ishasRole);
			// 使用check方法进行授权,如果授权不通过会抛出异常
			// subject.checkRole("admin");
			// hasAllRoles是否拥有多个角色
			boolean hasAllRoles = currentUser.hasAllRoles(Arrays.asList("superAdmin", "admin"));
			System.out.println("多个角色判断" + hasAllRoles);

			// 基于资源的授权,isPermitted传入权限标识符
			boolean isPermitted = currentUser.isPermitted("user:create");
			System.out.println("单个权限判断" + isPermitted);
			boolean isPermittedAll = currentUser.isPermittedAll("user:create", "user:delete");
			System.out.println("多个权限判断" + isPermittedAll);
			// 使用check方法进行授权,如果授权不通过会抛出异常
			// currentUser.checkPermission("items:create");
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		// 退出
		currentUser.logout();
		return null;
	}

	@Override
	public List<User> getList(User user) {
		return null;
	}
}