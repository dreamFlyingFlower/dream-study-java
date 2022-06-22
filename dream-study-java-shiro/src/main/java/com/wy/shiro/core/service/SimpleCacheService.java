package com.wy.shiro.core.service;

import org.apache.shiro.cache.Cache;

/**
 * 实现缓存管理服务
 * 
 * @author 飞花梦影
 * @date 2022-06-22 17:03:12
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface SimpleCacheService {

	/**
	 * 创建缓存
	 * 
	 * @param cacheName 缓存名称
	 * @param cache 缓存对象
	 */
	void creatCache(String cacheName, Cache<Object, Object> cache);

	/**
	 * 获得缓存
	 * 
	 * @param cacheName 缓存名称
	 * @return 缓存对象
	 */
	Cache<Object, Object> getCache(String cacheName);

	/**
	 * 删除缓存
	 * 
	 * @param cacheName 缓存名称
	 */
	void removeCache(String cacheName);

	/**
	 * 更新缓存
	 * 
	 * @param cacheName 缓存名称
	 * @param cache 新的缓存对象
	 */
	void updateCache(String cacheName, Cache<Object, Object> cache);
}