package com.wy.dynamicdb;

/**
 * 数据库主从类型,可多个从数据库,也可单个数据库.数据库主从之间有延迟,非即时性数据才可读从数据库
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 16:51:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum DBTypeEnum {

	MASTER,
	SLAVE1,
	SLAVE2;
}