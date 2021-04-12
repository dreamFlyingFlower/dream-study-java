package com.wy.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * cahce-redis的使用,redis可以换成ecache或其他类型缓存,查看{CacheProperties}中的cachetype
 * 不论那种cache技术,可以不重写CacheManager方法,也可重写.KeyGenerator方法生成cache的key
 * 
 * @author wanyang 2018年7月19日
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

	/**
	 * 自定义存入到缓存中的key值
	 */
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				List<String> key = new ArrayList<>();
				key.add(target.getClass().getName());
				key.add(method.getName());
				for (Object obj : params) {
					key.add(obj.toString());
				}
				return String.join("_", key);
			}
		};
	}
	
	/**
	 * 定义缓存到那种缓存技术中
	 */
	@Bean
	@ConditionalOnClass(RedisConfig.class)
	@ConditionalOnMissingBean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		return RedisCacheManager.create(connectionFactory);
	}
}