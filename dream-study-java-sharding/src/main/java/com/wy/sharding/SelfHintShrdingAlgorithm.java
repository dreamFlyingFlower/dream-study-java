package com.wy.sharding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

/**
 * 自定义强制路由分片算法
 *
 * @author 飞花梦影
 * @date 2022-10-05 15:51:44
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class SelfHintShrdingAlgorithm implements HintShardingAlgorithm<Long> {

	@Override
	public Collection<String> doSharding(Collection<String> arg0, HintShardingValue<Long> arg1) {
		// 添加分库或分表路由逻辑
		List<String> ret = new ArrayList<>();
		for (String string : arg0) {
			for (Long value : arg1.getValues()) {
				if (string.endsWith(String.valueOf(value % 2))) {
					ret.add(string);
				}
			}
		}
		return ret;
	}

}