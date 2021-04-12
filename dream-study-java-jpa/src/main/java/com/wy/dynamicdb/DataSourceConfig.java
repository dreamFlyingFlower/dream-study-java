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
 * @apiNote 数据源配置,可在配置文件中自定义,不必一定都写在springdatasource下
 * @author ParadiseWY
 * @date 2019年12月21日 下午3:14:51
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
		targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
		targetDataSources.put(DBTypeEnum.SLAVE1, slave1DataSource);
		targetDataSources.put(DBTypeEnum.SLAVE2, slave2DataSource);
		DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
		dynamicRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
		dynamicRoutingDataSource.setTargetDataSources(targetDataSources);
		return dynamicRoutingDataSource;
	}
}