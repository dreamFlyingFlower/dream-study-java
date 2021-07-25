package com.wy.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageInterceptor;

/**
 * 多数据源1,该数据源可以直接使用spring自动配置的数据源,注意,多数据源模式下,需要使用hikari的数据源
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 17:52:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@MapperScan(basePackages = "com.wy.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisMultiDBSource1Config {

	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * primary注解在多数据源模式下必须存在,该注解将指定一个数据源为默认数据源
	 * 
	 * @param dataSource 数据源
	 * @return sqlsessionfactory
	 * @throws Exception
	 */
	@Bean(name = "sqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml"));
		// 分页插件,多数据源模式下若数据源的数据源类型不同,则每个数据源都需要单独指定方言,否则报错
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.setProperty("helperDialect", "mysql");
		pageInterceptor.setProperties(properties);
		bean.setPlugins(new Interceptor[] { pageInterceptor });
		// 多数据源下,下划线自动转驼峰无效,需要每个数据源都指定转换方式
		bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
		bean.getObject().getConfiguration()
				.setObjectWrapperFactory(new MybatisUnderscore2CamelCaseConfig.MapWrapperFactory());
		return bean.getObject();
	}

	@Bean(name = "transactionManager")
	@Primary
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "sqlSessionTemplate")
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}