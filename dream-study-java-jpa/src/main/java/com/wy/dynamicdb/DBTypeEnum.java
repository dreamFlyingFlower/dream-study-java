package com.wy.dynamicdb;

/**
 * @apiNote 数据库主从类型,可多个从数据库,也可单个数据库.数据库主从之间有延迟,非即时性数据才可读从数据库
 * @author ParadiseWY
 * @date 2019年12月21日 下午2:58:17
 */
public enum DBTypeEnum {

	MASTER,SLAVE1,SLAVE2;
}