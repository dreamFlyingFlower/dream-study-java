package com.wy.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.wy.collection.ListTool;
import com.wy.model.Permission;
import com.wy.model.Role;
import com.wy.model.User;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义Shiro的登录校验方法
 *
 * @author 飞花梦影
 * @date 2022-06-09 10:58:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class SelfShiroRedisRealm extends AuthorizingRealm {

	@Autowired
	private RedisTemplate<String, String> stringRedisTemplate;

	/**
	 * 必须重写此方法,不然Shiro会报错
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof SelfShiroToken;
	}

	/**
	 * 授权,查询数据库获取用户的所有角色和权限信息
	 *
	 * @param principals 用户信息
	 * @return 授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.info("Shiro权限配置");
		// 同doGetAuthenticationInfo()返回值的第一个参数
		String token = principals.toString();
		User user = JSON.parseObject(stringRedisTemplate.opsForValue().get(token), User.class);
		List<Role> roles = user.getRoles();
		List<String> permissionNames = new ArrayList<>();
		List<String> roleNames = new ArrayList<>();
		if (ListTool.isNotEmpty(roles)) {
			for (Role role : roles) {
				roleNames.add(role.getRoleName());
				Set<Permission> permissions = role.getPermissions();
				if (ListTool.isNotEmpty(permissions)) {
					for (Permission permission : permissions) {
						permissionNames.add(permission.getName());
					}
				}
			}
		}
		// 设置用户角色和权限
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addStringPermissions(permissionNames);
		simpleAuthorizationInfo.addRoles(roleNames);
		return simpleAuthorizationInfo;
	}

	/**
	 * 认证登入
	 *
	 * @param token 用户登录的信息
	 * @return 认证信息
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("Shiror认证");
		SelfShiroToken usToken = (SelfShiroToken) token;
		// 获取用户的输入的账号
		String accessToken = (String) usToken.getCredentials();
		// token失效
		if (!stringRedisTemplate.hasKey(accessToken)) {
			throw new IncorrectCredentialsException("token失效,请重新登录");
		}
		// token 没失效就刷新token
		stringRedisTemplate.expire(accessToken, 30, TimeUnit.MINUTES);
		return new SimpleAuthenticationInfo(accessToken, accessToken, getName());
	}
}