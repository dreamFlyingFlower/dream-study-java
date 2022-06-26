package com.wy.shiro.core.service.impl;

import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.Cache;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wy.shiro.core.ShiroRedissionSerialize;
import com.wy.shiro.core.service.SimpleCacheService;
import com.wy.shiro.utils.ShiroUtil;

/**
 * 实现缓存管理服务
 * 
 * @author 飞花梦影
 * @date 2022-06-22 17:05:10
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
public class SimpleCacheServiceImpl implements SimpleCacheService {

	@Autowired
	private RedissonClient redissonClient;

	@Override
	public void creatCache(String cacheName, Cache<Object, Object> cache) {
		RBucket<String> bucket = redissonClient.getBucket(cacheName);
		bucket.trySet(ShiroRedissionSerialize.serialize(cache), ShiroUtil.getShiroSession().getTimeout() / 1000,
				TimeUnit.SECONDS);
	}

	@Override
	public Cache<Object, Object> getCache(String cacheName) {
		RBucket<String> bucket = redissonClient.getBucket(cacheName);
		return JSON.parseObject(JSON.toJSONString(ShiroRedissionSerialize.deserialize(bucket.get())),
				new TypeReference<Cache<Object, Object>>() {
				});
	}

	@Override
	public void removeCache(String cacheName) {
		RBucket<String> bucket = redissonClient.getBucket(cacheName);
		bucket.delete();
	}

	@Override
	public void updateCache(String cacheName, Cache<Object, Object> cache) {
		RBucket<String> bucket = redissonClient.getBucket(cacheName);
		bucket.set(ShiroRedissionSerialize.serialize(cache), ShiroUtil.getShiroSession().getTimeout() / 1000,
				TimeUnit.SECONDS);
	}
}