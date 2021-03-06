package com.wy.shiro.config;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.wy.shiro.interceptor.ModifyArgsValueInterceptor;
import com.wy.shiro.properties.ShiroDataSourceProperties;
import com.wy.shiro.utils.IpAddrUtil;
import com.wy.shiro.utils.SeqGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@MapperScan(basePackages = { "com.wy.shiro.mapper" }, sqlSessionFactoryRef = "sqlSessionFactoryBeanShiro")
@EnableConfigurationProperties({ ShiroDataSourceProperties.class })
public class MybatisConfigShiro {

	@Autowired
	ShiroDataSourceProperties dataSourceProperties;

	private AtomikosDataSourceBean handleXaProperties(AtomikosDataSourceBean atomikosDataSourceBean) {
		Properties dataSourceHashMap = new Properties();
		dataSourceHashMap.put("driverClassName", dataSourceProperties.getDriverClassName());
		dataSourceHashMap.put("url", dataSourceProperties.getUrl());
		dataSourceHashMap.put("password", dataSourceProperties.getPassword());
		dataSourceHashMap.put("username", dataSourceProperties.getUsername());
		dataSourceHashMap.put("minIdle", dataSourceProperties.getMinIdle());
		dataSourceHashMap.put("maxActive", dataSourceProperties.getMaxActive());
		dataSourceHashMap.put("maxWait", dataSourceProperties.getMaxWait());
		dataSourceHashMap.put("validationQuery", dataSourceProperties.getValidationQuery());
		dataSourceHashMap.put("testOnBorrow", dataSourceProperties.getTestOnBorrow());
		dataSourceHashMap.put("testOnReturn", dataSourceProperties.getTestOnReturn());
		dataSourceHashMap.put("testWhileIdle", dataSourceProperties.getTestWhileIdle());
		dataSourceHashMap.put("logAbandoned", dataSourceProperties.getLogAbandoned());
		dataSourceHashMap.put("filters", dataSourceProperties.getFilters());
		dataSourceHashMap.put("initialSize", dataSourceProperties.getInitialSize());
		dataSourceHashMap.put("maxEvictableIdleTimeMillis", dataSourceProperties.getMaxLifeTime());
		atomikosDataSourceBean.setXaProperties(dataSourceHashMap);
		return atomikosDataSourceBean;
	}

	@Bean(name = "dataSourceShiro", initMethod = "init", destroyMethod = "close")
	public AtomikosDataSourceBean dataSourceShiro() {
		log.info("dataSourceShiro:?????????????????????!IP=" + IpAddrUtil.hostIpAddr());
		AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
		atomikosDataSourceBean.setUniqueResourceName(dataSourceProperties.getDataSourceName());
		atomikosDataSourceBean.setXaDataSourceClassName(dataSourceProperties.getXaDataSourceClassName());

		// ???????????????????????????
		atomikosDataSourceBean.setMinPoolSize(dataSourceProperties.getMinIdle());
		// ???????????????????????????
		atomikosDataSourceBean.setMaxPoolSize(dataSourceProperties.getMaxActive());
		// ??????????????????????????????????????????
		atomikosDataSourceBean.setBorrowConnectionTimeout(dataSourceProperties.getMaxWait());
		// ????????????????????????(?????????5??????)
		atomikosDataSourceBean.setReapTimeout(20);
		// ??????????????????,????????????????????????????????????????????????
		atomikosDataSourceBean.setMaxIdleTime(60);
		// ??????????????????
		atomikosDataSourceBean.setMaintenanceInterval(60);
		atomikosDataSourceBean.setMaxLifetime(dataSourceProperties.maxLifeTime);
		try {
			// ???????????????????????????datasources??????
			atomikosDataSourceBean.setLoginTimeout(60);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("dataSourceShiro????????????!IP=" + IpAddrUtil.hostIpAddr());
		}
		atomikosDataSourceBean.setTestQuery(dataSourceProperties.getValidationQuery());
		handleXaProperties(atomikosDataSourceBean);

		log.info("dataSourceShiro:?????????????????????!!IP=" + IpAddrUtil.hostIpAddr());
		return atomikosDataSourceBean;
	}

	@Bean(name = "sqlSessionFactoryBeanShiro")
	public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSourceShiro());
		factoryBean.setMapperLocations(
		        new PathMatchingResourcePatternResolver().getResources(dataSourceProperties.getMapperLocations()));
		factoryBean.setTypeAliasesPackage(dataSourceProperties.getTypeAliasesPackage());
		factoryBean.setPlugins(new ModifyArgsValueInterceptor(seqGeneratorShiro(), dataSourceProperties.getWorkId(),
		        dataSourceProperties.getPrimaryKey()));
		// ??????????????????
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setLogImpl(Log4j2Impl.class);
		factoryBean.setConfiguration(configuration);
		return factoryBean;
	}

	@Bean("seqGeneratorShiro")
	public SeqGenerator seqGeneratorShiro() {
		return new SeqGenerator(dataSourceProperties.getWorkId());
	}
}