package com.wy.sharding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Range;

/**
 * 范围分库策略
 * 
 * @author 飞花梦影
 * @date 2021-11-22 23:33:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class RangeDbShardingAlgorithm implements RangeShardingAlgorithm<Long> {

	@Override
	public Collection<String> doSharding(Collection<String> availableTargetNames,
			RangeShardingValue<Long> shardingValue) {
		System.out.println("Range collection:" + JSON.toJSONString(availableTargetNames) + ",rangeShardingValue:"
				+ JSON.toJSONString(shardingValue));
		Collection<String> collect = new ArrayList<>();
		Range<Long> valueRange = shardingValue.getValueRange();
		// BETWEEN AND中分片键对应的最小值
		long lowerEndpoint = Long.parseLong(String.valueOf(valueRange.lowerEndpoint()));
		// BETWEEN AND中分片键对应的最大值
		long upperEndpoint = Long.parseLong(String.valueOf(valueRange.upperEndpoint()));
		// 分表的后缀
		int i = 1;
		List<Integer> arrs = new ArrayList<Integer>();
		// 求分表后缀名的递归算法
		List<Integer> list = countDatabaseNum(i, lowerEndpoint, upperEndpoint, arrs);
		for (Integer integer : list) {
			for (String each : availableTargetNames) {
				if (each.endsWith("_" + integer)) {
					collect.add(each);
				}
			}
		}
		return collect;
	}

	/**
	 * 计算该量级的数据在哪个表
	 * 
	 * @param columnValue
	 * @param i
	 * @param lowerEndpoint 最小区间
	 * @param upperEndpoint 最大区间
	 * @return
	 */
	private List<Integer> countDatabaseNum(int i, long lowerEndpoint, long upperEndpoint, List<Integer> arrs) {
		long left = ShardingConstant.DATABASE_AMOUNT * (i - 1);
		long right = ShardingConstant.DATABASE_AMOUNT * i;
		// 区间最大值小于分库最大值
		if (left < upperEndpoint && upperEndpoint <= right) {
			arrs.add(i);
			return arrs;
		} else {
			if (left < lowerEndpoint && lowerEndpoint <= right) {
				arrs.add(i);
			}
			i++;
			return countDatabaseNum(i, lowerEndpoint, upperEndpoint, arrs);
		}
	}
}