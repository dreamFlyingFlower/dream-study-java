package com.wy.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageInterceptor;
import com.wy.properties.ConfigProperties;

/**
 * 多数据源2
 * 
 * @author ParadiseWY
 * @date 2020年7月7日 下午4:45:54
 */
// @Configuration
// @MapperScan(basePackages = "com.wy.mapper1", sqlSessionTemplateRef = "sqlSessionTemplate1")
public class MybatisMultiDBSource2Config {

	@Autowired
	private ConfigProperties config;

	@Value("${spring.datasource.type}")
	private Class<? extends DataSource> type;

	@Bean(name = "dataSource1")
	@ConfigurationProperties(prefix = "config.data-source1")
	public DataSource dataSource() {
		return DataSourceBuilder.create().type(type).driverClassName(config.getDataSource1().getDriverClassName())
				.url(config.getDataSource1().getUrl()).username(config.getDataSource1().getUsername())
				.password(config.getDataSource1().getPassword()).build();
	}

	@Bean(name = "sqlSessionFactory1")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource1") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers1/*.xml"));
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.setProperty("helperDialect", "sqlserver");
		properties.setProperty("reasonable", "false");
		pageInterceptor.setProperties(properties);
		bean.setPlugins(new Interceptor[] { pageInterceptor });
		bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
		bean.getObject().getConfiguration()
				.setObjectWrapperFactory(new MybatisUnderscore2CamelCaseConfig.MapWrapperFactory());
		return bean.getObject();
	}

	@Bean(name = "transactionManager1")
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource1") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "sqlSessionTemplate1")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}