package com.wy.shiro.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.wy.shiro.constant.CacheConstant;

/**
 * 自定义统一sessiondao实现,重写会话的创建,读取,修改等操作,全部缓存与redis中
 * 
 * @author 飞花梦影
 * @date 2022-06-22 16:09:14
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class RedisSessionDao extends AbstractSessionDAO {

	@Autowired
	private RedissonClient redissonClient;

	private long globalSessionTimeout;

	/**
	 * 创建session
	 * 
	 * @param session 会话对象
	 * @return
	 */
	@Override
	protected Serializable doCreate(Session session) {
		// 创建唯一标识的sessionId
		Serializable sessionId = generateSessionId(session);
		// 为session会话指定唯一的sessionId
		assignSessionId(session, sessionId);
		// 放入缓存中
		String key = CacheConstant.GROUP_CAS + sessionId.toString();
		RBucket<String> bucket = redissonClient.getBucket(key);
		bucket.trySet(ShiroRedissionSerialize.serialize(session), globalSessionTimeout / 1000, TimeUnit.SECONDS);
		return sessionId;
	}

	/**
	 * 读取session
	 * 
	 * @param sessionId 唯一标识
	 * @return
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		String key = CacheConstant.GROUP_CAS + sessionId.toString();
		RBucket<String> bucket = redissonClient.getBucket(key);
		return (Session) ShiroRedissionSerialize.deserialize(bucket.get());
	}

	/**
	 * 更新session
	 * 
	 * @param session 对象
	 * @return
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		String key = CacheConstant.GROUP_CAS + session.getId().toString();
		RBucket<String> bucket = redissonClient.getBucket(key);
		bucket.set(ShiroRedissionSerialize.serialize(session), globalSessionTimeout / 1000, TimeUnit.SECONDS);
	}

	/**
	 * 删除session
	 * 
	 * @param
	 */
	@Override
	public void delete(Session session) {
		String key = CacheConstant.GROUP_CAS + session.getId().toString();
		RBucket<String> bucket = redissonClient.getBucket(key);
		bucket.delete();
	}

	/**
	 * 统计当前活跃用户数(后续扩展)
	 * 
	 * @return
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		return Collections.emptySortedSet();
	}

	public void setGlobalSessionTimeout(long globalSessionTimeout) {
		this.globalSessionTimeout = globalSessionTimeout;
	}
}