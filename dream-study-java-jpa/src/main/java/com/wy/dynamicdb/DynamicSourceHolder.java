package com.wy.dynamicdb;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过AOP来判断使用哪一个标识数据库
 *
 * @author 飞花梦影
 * @date 2019-08-18 22:07:54
 * @git {@link https://github.com/mygodness100}
 */
public class DynamicSourceHolder {

	private static final ThreadLocal<DBTypeEnum> dbLocal = new ThreadLocal<>();

	private static final AtomicInteger counter = new AtomicInteger(-1);

	public static void setDataSourceKey(DBTypeEnum dbTypeEnum) {
		dbLocal.set(dbTypeEnum);
	}

	public static DBTypeEnum getDataSourceKey() {
		DBTypeEnum dbTypeEnum = dbLocal.get();
		if (null == dbTypeEnum) {
			setMaster();
		}
		return dbLocal.get();
	}

	public static void setMaster() {
		setDataSourceKey(DBTypeEnum.MASTER);
	}

	/**
	 * 清理连接类型
	 */
	public static void clearDBType() {
		dbLocal.remove();
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