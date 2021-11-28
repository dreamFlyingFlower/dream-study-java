package com.wy.sharding;

import java.util.Collection;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import com.alibaba.fastjson.JSON;

/**
 * 自定义精准分库策略
 * 
 * @author 飞花梦影
 * @date 2021-11-22 23:32:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class PreciseDbShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

	/**
	 * 精准分库,根据分库字段的值查找该值具体在哪一个数据库
	 * 
	 * @param availableTargetNames 可用数据库名列表
	 * @param shardingValue 分库字段对象
	 * @return 具体的数据库
	 */
	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
		System.out.println("collection:" + JSON.toJSONString(availableTargetNames) + ",shardingValue:"
				+ JSON.toJSONString(shardingValue));
		for (String name : availableTargetNames) {
			// =与IN中分片键对应的值
			String value = String.valueOf(shardingValue.getValue());
			// 分库的后缀
			int i = 1;
			// 求分库后缀名的递归算法
			if (name.endsWith("_" + countDatabaseNum(Long.parseLong(value), i))) {
				return name;
			}
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * 计算该量级的数据在哪个数据库,有缺陷
	 * 
	 * @param columnValue 用于分库的字段值
	 * @param i 分库的后缀
	 * @return 具体的数据库
	 */
	private String countDatabaseNum(long columnValue, int i) {
		// 每个库中定义的数据量
		long left = ShardingConstant.DATABASE_AMOUNT * (i - 1);
		long right = ShardingConstant.DATABASE_AMOUNT * i;
		if (left < columnValue && columnValue <= right) {
			return String.valueOf(i);
		} else {
			i++;
			return countDatabaseNum(columnValue, i);
		}
	}
}