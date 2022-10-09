package com.wy.sharding;

import java.util.Properties;

import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

/**
 * 自定义生成key策略
 *
 * @author 飞花梦影
 * @date 2022-10-05 15:06:17
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class SelfShardingKeyGenerator implements ShardingKeyGenerator {

	@Override
	public Properties getProperties() {
		return null;
	}

	/**
	 * 该方法需要实现为配置文件使用,如sharding默认的2种方式为UUID,SNOWFLAKE,此处定义后可以类似使用
	 */
	@Override
	public String getType() {
		return "SelfShardingKeyGenerator";
	}

	@Override
	public void setProperties(Properties arg0) {

	}

	@Override
	public Comparable<?> generateKey() {
		// 生成key的算法
		return null;
	}
}