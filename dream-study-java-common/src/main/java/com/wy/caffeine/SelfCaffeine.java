package com.wy.caffeine;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Caffeine缓存
 *
 * @author 飞花梦影
 * @date 2023-04-14 20:55:24
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class SelfCaffeine {

	public static void main(String[] args) {
		Cache<String, String> cache = Caffeine.newBuilder().initialCapacity(5)
				// 超出时淘汰
				.maximumSize(10)
				// 设置写缓存后n秒钟过期
				.expireAfterWrite(60, TimeUnit.SECONDS)
				// 设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite
				// .expireAfterAccess(17, TimeUnit.SECONDS)
				.build();

		String orderId = String.valueOf(123456789);
		cache.get(orderId, key -> {
			System.out.println(key);
			return key;
		});
	}
}