package com.wy.retry.core.impl;

import java.util.concurrent.TimeUnit;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

/**
 * 自定义密码比较器,超过指定次数后限制登录,完成后需要添加到 {@link SelfShiroRealm#initCredentialsMatcher()}中
 * 
 * 保证原子性:单系统-AtomicLong计数;集群系统-RedissionClient提供的RAtomicLong计数
 * 
 * 步骤:
 * 
 * <pre>
 * 获取系统中是否已有登录次数缓存,缓存对象结构预期为:用户名--登录次数
 * 如果之前没有登录缓存,则创建一个登录次数缓存
 * 如果缓存次数已经超过限制,则驳回本次登录请求
 * 将缓存记录的登录次数加1,设置指定时间内有效
 * 验证用户本次输入的帐号密码,如果登录登录成功,则清除掉登录次数的缓存
 * AuthenticatingRealm里有比较密码的入口doCredentialsMatch方法,可继承其实现方法实现限制
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2022-06-21 17:39:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

	private RedissonClient redissonClient;

	private static Long RETRY_LIMIT_NUM = 4L;

	public RetryLimitCredentialsMatcher(String hashAlgorithmName, RedissonClient redissonClient) {
		super(hashAlgorithmName);
		this.redissonClient = redissonClient;
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// 获得登录token
		String loginName = (String) token.getPrincipal();
		// 获取系统中是否已有登录次数缓存,缓存对象结构预期为:用户名--登录次数
		RAtomicLong atomicLong = redissonClient.getAtomicLong(loginName);
		// 如果之前没有登录缓存,则创建一个登录次数缓存
		long retryFlat = atomicLong.get();
		// 判断是否超过次数
		if (retryFlat > RETRY_LIMIT_NUM) {
			// 如果缓存次数已经超过限制,则驳回本次登录请求,且10分钟后重试
			atomicLong.expire(10, TimeUnit.MINUTES);
			throw new ExcessiveAttemptsException("密码次数错误5次,请10分钟后重试");
		}
		// 将缓存记录的登录次数加1,设置指定时间内有效
		atomicLong.incrementAndGet();
		atomicLong.expire(10, TimeUnit.MINUTES);
		// 验证用户本次输入的帐号密码,如果登录登录成功,则清除掉登录次数的缓存
		boolean flag = super.doCredentialsMatch(token, info);
		if (flag) {
			atomicLong.delete();
		}
		return flag;
	}
}