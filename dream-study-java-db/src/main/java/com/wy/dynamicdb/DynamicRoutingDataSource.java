package com.wy.dynamicdb;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * 数据库读写分离动态数据源,继承AbstractRoutingDataSource,重写determineCurrentLookupKey()
 * 
 * <pre>
 * {@link AbstractRoutingDataSource}:单例,线程不安全,需要使用threadlocal来保证线程安全
 * {@link AbstractRoutingDataSource#setTargetDataSources}:设置多数据源,key需要唯一
 * {@link AbstractRoutingDataSource#setDefaultTargetDataSource}:设置默认数据源
 * {@link DataSourceConfig}:配置AbstractRoutingDataSource中的多数据源和默认数据源
 * </pre>
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 15:55:22
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

	/**
	 * 决定使用那个数据源,返回的是targetDataSources中的key
	 */
	@Nullable
	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicSourceHolder.getDataSourceKey();
	}
}