package com.wy.dynamicdb;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * @apiNote 数据库读写分离.定义动态数据源,继承spring提供的抽象类,重写determineCurrentLookupKey方法.
 *          由于DynamicSourceConfig是单例的,线程不安全,需要使用threadlocal来保证线程安全.
 *          需要在spring的配置文件配置每一个key所指向的数据源以及默认数据库,可查看{@link AbstractRoutingDataSource}中的属性.
 *          targetDataSources:设置多数据源,defaultTargetDataSource设置默认数据源
 *          通过对接口的调用,拦截接口的名称,判断是读或写,从DynamicSourceHolder中获得数据源
 * @author ParadiseWY
 * @date 2019年8月18日 下午10:03:17
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

	@Nullable
	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicSourceHolder.getDataSourceKey();
	}
}