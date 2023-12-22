package com.wy.mybatis;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.dynamicdb.DSType;
import com.wy.dynamicdb.DynamicRoutingDataSource;

/**
 * 多数据源配置:<br>
 * 1.多数据源模式下,在application配置文件中的大部分全局配置无效
 * 2.多数据源需要单独的配置文件,每一个sqlsessionfactorybean都需要一个单独数据源
 * 3.该模式下,map-underscore-to-camel-case:true无效,mybatis不会自动将map中的下划线转驼峰
 * 若仍然需要该特性,查询{@link MybatisUnderscore2CamelCaseConfig}
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 17:52:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MybatisMultiConfig {

	@Bean
	DataSource dynamicRoutingDataSource(@Qualifier("dataSource") DataSource masterDataSource,
			@Qualifier("dataSource1") DataSource slave1DataSource) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DSType.MASTER, masterDataSource);
		targetDataSources.put(DSType.SLAVE1, slave1DataSource);
		DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource(null);
		dynamicRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
		dynamicRoutingDataSource.setTargetDataSources(targetDataSources);
		return dynamicRoutingDataSource;
	}
}