package com.wy.dynamicdb;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;

import dream.flying.flower.collection.ListHelper;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

	public DynamicRoutingDataSource(List<DruidDataSourceWrapper> druidDataSourceWrappers) {
		// super.setDefaultTargetDataSource(defaultDataSource);
		// super.setTargetDataSources(targetDataSources);
		createDataSource(druidDataSourceWrappers);
	}

	/**
	 * 决定使用那个数据源,返回的是targetDataSources中的key
	 */
	@Nullable
	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicSourceHolder.getDataSourceKey();
	}

	/**
	 * 添加数据源信息
	 * 
	 * @param druidDataSourceWrappers 数据源实体集合
	 */
	private void createDataSource(List<DruidDataSourceWrapper> druidDataSourceWrappers) {
		if (ListHelper.isEmpty(druidDataSourceWrappers)) {
			return;
		}
		try {
			Map<Object, Object> targetDataSourceMap = new HashMap<>();
			for (DruidDataSourceWrapper ds : druidDataSourceWrappers) {
				// name作为key,必填且必须唯一
				if (StrHelper.isBlank(ds.getName())) {
					continue;
				}
				Class.forName(ds.getDriverClassName());
				DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
				ds.init();
				targetDataSourceMap.put(ds.getName(), ds);
			}
			super.setTargetDataSources(targetDataSourceMap);
			// 将TargetDataSources中的连接信息放入resolvedDataSources管理
			super.afterPropertiesSet();
		} catch (ClassNotFoundException | SQLException e) {
			log.error("---程序报错---:{}", e.getMessage());
		}
	}
}