package com.wy.retry.core.impl;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.wy.retry.constant.CacheConstant;
import com.wy.retry.constant.SuperConstant;
import com.wy.retry.core.ShiroDbRealm;
import com.wy.retry.core.SimpleCacheService;
import com.wy.retry.core.base.ShiroUser;
import com.wy.retry.core.base.SimpleToken;
import com.wy.retry.core.bridge.UserBridgeService;
import com.wy.retry.pojo.User;
import com.wy.retry.utils.BeanConv;
import com.wy.retry.utils.EmptyUtil;
import com.wy.retry.utils.ShiroUtil;

/**
 * 自定义realm的抽象类实现
 * 
 * @author 飞花梦影
 * @date 2022-06-21 17:44:18
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class SelfShiroRealm extends ShiroDbRealm {

	@Autowired
	UserBridgeService userBridgeService;

	@Autowired
	SimpleCacheService simpleCacheService;

	@Resource(name = "redissonClientForShiro")
	private RedissonClient redissonClient;

	@Override
	public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// token令牌信息
		SimpleToken simpleToken = (SimpleToken) token;
		// 查询user对象
		User user = userBridgeService.findUserByLoginName(simpleToken.getUsername());
		if (EmptyUtil.isNullOrEmpty(user)) {
			throw new UnknownAccountException("账号不存在！");
		}
		// 构建认证令牌对象
		ShiroUser shiroUser = BeanConv.toBean(user, ShiroUser.class);
		shiroUser.setResourceIds(userBridgeService.findResourcesIds(shiroUser.getId()));
		String slat = shiroUser.getSalt();
		String password = shiroUser.getPassWord();
		// 构建认证信息对象:1、令牌对象 2、密文密码 3、加密因子 4、当前realm的名称
		return new SimpleAuthenticationInfo(shiroUser, password, ByteSource.Util.bytes(slat), getName());
	}

	@Override
	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		return userBridgeService.getAuthorizationInfo(shiroUser);
	}

	@Override
	protected void doClearCache(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		String sessionId = ShiroUtil.getShiroSessionId();
		String roleKey = CacheConstant.ROLE_KEY + sessionId;
		String resourcesKey = CacheConstant.RESOURCES_KEY + sessionId;
		String loginNamekey = CacheConstant.FIND_USER_BY_LOGINNAME + shiroUser.getLoginName();
		String resourcesIdKey = CacheConstant.RESOURCES_KEY_IDS + sessionId;
		simpleCacheService.removeCache(roleKey);
		simpleCacheService.removeCache(resourcesKey);
		simpleCacheService.removeCache(loginNamekey);
		simpleCacheService.removeCache(resourcesIdKey);
		super.doClearCache(principals);
	}

	@Override
	public void initCredentialsMatcher() {
		// 指定密码算法
		// HashedCredentialsMatcher hashedCredentialsMatcher = new
		// HashedCredentialsMatcher(SuperConstant.HASH_ALGORITHM);
		// 指定密码算法,密码重试限制
		RetryLimitCredentialsMatcher hashedCredentialsMatcher =
		        new RetryLimitCredentialsMatcher(SuperConstant.HASH_ALGORITHM, redissonClient);
		// 指定迭代次数
		hashedCredentialsMatcher.setHashIterations(SuperConstant.HASH_INTERATIONS);
		// 生效密码比较器
		setCredentialsMatcher(hashedCredentialsMatcher);
	}
}