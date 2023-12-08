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

	private static final ThreadLocal<DSType> DS_CURRENT = new ThreadLocal<>();

	private static final AtomicInteger COUNTER = new AtomicInteger(-1);

	public static void setDataSourceKey(DSType dsType) {
		DS_CURRENT.set(dsType);
	}

	public static DSType getDataSourceKey() {
		DSType dsType = DS_CURRENT.get();
		if (null == dsType) {
			setMaster();
		}
		return DS_CURRENT.get();
	}

	public static void setMaster() {
		setDataSourceKey(DSType.MASTER);
	}

	/**
	 * 清理连接类型
	 */
	public static void clear() {
		DS_CURRENT.remove();
	}

	/**
	 * 若为单备数据库,则直接可返回SLAVE,若为多备数据库,则可轮询或其他算法
	 */
	public static void setSalve() {
		int index = COUNTER.getAndIncrement() % 2;
		if (COUNTER.get() > 99999) {
			COUNTER.set(-1);
		}
		if (index == 0) {
			setDataSourceKey(DSType.SLAVE1);
		} else {
			setDataSourceKey(DSType.SLAVE2);
		}
	}
}