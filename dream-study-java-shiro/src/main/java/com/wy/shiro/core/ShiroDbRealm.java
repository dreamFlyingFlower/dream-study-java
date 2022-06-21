package com.wy.shiro.core;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * shiro自定义realm
 *
 * @author 飞花梦影
 * @date 2022-06-21 23:26:29
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public abstract class ShiroDbRealm extends AuthorizingRealm {

	/**
	 * 认证
	 * 
	 * @param authcToken token对象
	 * @return
	 */
	@Override
	public abstract AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken);

	/**
	 * 鉴权
	 * 
	 * @param principals 令牌
	 * @return
	 */
	@Override
	public abstract AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals);

	/**
	 * 密码匹配器
	 */
	@PostConstruct
	public abstract void initCredentialsMatcher();
}