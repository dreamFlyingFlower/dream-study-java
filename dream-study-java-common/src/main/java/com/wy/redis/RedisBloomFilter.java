package com.wy.redis;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * 布隆过滤器,解决Redis缓存击穿
 *
 * @author 飞花梦影
 * @date 2023-02-25 17:16:29
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class RedisBloomFilter {

	public void name() {
		// 第2个参数为缓存容量
		BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 10000);
		// 第3个参数为误判概率,默认为0.03,值越小,布隆过滤器的长度越大,如设置10000,根据误判概率,实际长度只有9700
		// 误判率越小,效率越低
		BloomFilter.create(Funnels.integerFunnel(), 10000, 0.01);
		// 往容器中存数据
		bloomFilter.put(11);
		// 判断某个值是否在过滤器中,不能精准判断,有误差
		System.out.println(bloomFilter.mightContain(112));
	}
}