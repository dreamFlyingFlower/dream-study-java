package com.wy.dynamicdb;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库读写分离多数据源配置,可在配置文件中自定义,适合固定数据源的小项目.
 * 
 * 主要通过对接口名称的拦截,动态判断是读或写,从 DynamicSourceHolder 中获得数据源
 * 
 * <pre>
 * {@link DynamicRoutingDataSource}:数据源动态切换配置
 * {@link DynamicSourceHolder}:线程安全的数据源切换规则
 * {@link DSMasterAspect DBSelectAspect}:数据源切换拦截器,主要拦截数据库执行的方法以及数据源
 * {@link DSMaster  DBSelect}:根据不同业务需要在不同的类或方法上添加注解,强制使用主数据源或指定数据源
 * {@link MybatisInterceptor}:使用AOP的局限性和复杂性比较大,如果使用了Mybatis,可以加入该类,不使用拦截器
 * </pre>
 * 
 * 需要在启动时剔除DataSourceAutoConfiguration.class的自动配置,否则可能报错
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 16:42:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class DataSourceConfig {

	@Bean
	@ConfigurationProperties("spring.datasource.master")
	public DataSource masterDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties("spring.datasource.slave1")
	public DataSource slave1DataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties("spring.datasource.slave2")
	public DataSource slave2DataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public DataSource dynamicRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
			@Qualifier("slave1DataSource") DataSource slave1DataSource,
			@Qualifier("slave2DataSource") DataSource slave2DataSource) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DSType.MASTER, masterDataSource);
		targetDataSources.put(DSType.SLAVE1, slave1DataSource);
		targetDataSources.put(DSType.SLAVE2, slave2DataSource);
		DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
		dynamicRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
		dynamicRoutingDataSource.setTargetDataSources(targetDataSources);
		return dynamicRoutingDataSource;
	}
}