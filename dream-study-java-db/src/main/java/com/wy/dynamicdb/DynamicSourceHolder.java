package com.wy.dynamicdb;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过AOP来判断使用哪一个标识数据库
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 16:06:36
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DynamicSourceHolder {

	private static final ThreadLocal<DBTypeEnum> DB_LOCAL = new ThreadLocal<>();

	private static final AtomicInteger counter = new AtomicInteger(-1);

	public static void setDataSourceKey(DBTypeEnum dbTypeEnum) {
		DB_LOCAL.set(dbTypeEnum);
	}

	public static DBTypeEnum getDataSourceKey() {
		DBTypeEnum dbTypeEnum = DB_LOCAL.get();
		if (null == dbTypeEnum) {
			setMaster();
		}
		return DB_LOCAL.get();
	}

	public static void setMaster() {
		setDataSourceKey(DBTypeEnum.MASTER);
	}

	/**
	 * 清理连接类型
	 */
	public static void clearDBType() {
		DB_LOCAL.remove();
	}

	/**
	 * 若为单备数据库,则直接可返回SLAVE,若为多备数据库,则可轮询或其他算法
	 */
	public static void setSalve() {
		int index = counter.getAndIncrement() % 2;
		if (counter.get() > 99999) {
			counter.set(-1);
		}
		if (index == 0) {
			setDataSourceKey(DBTypeEnum.SLAVE1);
		} else {
			setDataSourceKey(DBTypeEnum.SLAVE2);
		}
	}
}