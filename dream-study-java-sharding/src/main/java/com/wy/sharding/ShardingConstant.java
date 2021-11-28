package com.wy.sharding;

/**
 * Sharding中用到的相关参数
 * 
 * @author 飞花梦影
 * @date 2021-11-22 23:35:22
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ShardingConstant {

	/**
	 * 订单,优惠券相关的表,按用户数量分库,64w用户数据为一个库
	 */
	int DATABASE_AMOUNT = 640000;

	/**
	 * 一个订单表里存10000的用户订单
	 */
	int TABLE_AMOUNT = 10000;
}