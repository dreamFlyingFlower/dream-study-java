package com.wy.dynamicdb;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Mybatis数据源拦截插件,需要调用{@link SqlSessionFactoryBean#setPlugins}方法将其添加进去
 * 
 * {@link Intercepts}:该注解必须加上,告诉mybatis那些类需要拦截
 * {@link Signature}:表明当前插件用来拦截那个对象的那个方法,以及参数类型,可以有多个
 * 
 * @auther 飞花梦影
 * @date 2021-07-24 16:55:03
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query",
				args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class MybatisInterceptor implements Interceptor {

	/**
	 * 拦截,拦截目标对象的目标方法的执行
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 判断是否存在事务操作
		boolean active = TransactionSynchronizationManager.isActualTransactionActive();
		// 参数需要根据Intercepts注解中的顺序来获得
		Object[] args = invocation.getArgs();
		if (active) {
			// 有事务的操作一定在主库中执行
			DynamicSourceHolder.setDataSourceKey(DSType.MASTER);
		} else {
			// 无事务的方法可以在从库中执行
			MappedStatement mappedStatement = (MappedStatement) args[0];
			// 判断方法类型
			if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
				// 在select语句中使用了last_insert_id函数
				if (mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
					DynamicSourceHolder.setDataSourceKey(DSType.MASTER);
				} else {
					DynamicSourceHolder.setDataSourceKey(DSType.SLAVE1);
				}
			}
		}
		// 返回执行后的返回值
		return invocation.proceed();
	}

	/**
	 * 包装目标对象,为目标对象创建一个代理对象
	 */
	@Override
	public Object plugin(Object target) {
		return Interceptor.super.plugin(target);
	}

	/**
	 * 将插件注册时的property属性设置进来
	 */
	@Override
	public void setProperties(Properties properties) {
		Interceptor.super.setProperties(properties);
	}
}