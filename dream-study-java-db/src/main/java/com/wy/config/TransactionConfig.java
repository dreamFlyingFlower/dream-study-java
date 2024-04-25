package com.wy.config;

import javax.sql.DataSource;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 事务拦截
 * 
 * @author 飞花梦影
 * @date 2021-01-07 14:19:34
 * @git {@link https://github.com/mygodness100}
 */
@EnableAspectJAutoProxy
@EnableTransactionManagement
@Aspect
@Configuration
public class TransactionConfig {

	/**
	 * 初始化事务管理器
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean
	TransactionManager mysqlTransactionManager(DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}

	/**
	 * 设置事务拦截器
	 * 
	 * @param dataSourceTransactionManager
	 * @return
	 */
	@Bean
	TransactionInterceptor mysqlTxAdvice(@Qualifier("mysqlTransactionManager") TransactionManager transactionManager) {
		// 默认事务
		DefaultTransactionAttribute defAttr =
				new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		// 查询只读事务
		DefaultTransactionAttribute queryAttr =
				new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		queryAttr.setReadOnly(true);
		// 设置拦截的方法
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		source.addTransactionalMethod("save*", defAttr);
		source.addTransactionalMethod("insert*", defAttr);
		source.addTransactionalMethod("delete*", defAttr);
		source.addTransactionalMethod("update*", defAttr);
		source.addTransactionalMethod("exec*", defAttr);
		source.addTransactionalMethod("set*", defAttr);
		source.addTransactionalMethod("add*", defAttr);
		source.addTransactionalMethod("get*", queryAttr);
		source.addTransactionalMethod("query*", queryAttr);
		source.addTransactionalMethod("find*", queryAttr);
		source.addTransactionalMethod("list*", queryAttr);
		source.addTransactionalMethod("count*", queryAttr);
		source.addTransactionalMethod("is*", queryAttr);
		return new TransactionInterceptor(transactionManager, source);
	}

	@Bean
	Advisor txAdviceAdvisor(@Qualifier("mysqlTxAdvice") TransactionInterceptor mysqlTxAdvice) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		return new DefaultPointcutAdvisor(pointcut, mysqlTxAdvice);
	}
}