package com.wy.mybatis;

import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * 自定义MyBatis插件,需要调用{@link SqlSessionFactoryBean#setPlugins}方法将其添加进去
 * 
 * Intercepts:该注解必须加上,告诉mybatis那些类需要拦截<br>
 * Signature:表明当前插件用来拦截那个对象的那个方法,以及参数类型,可以有多个
 * 
 * @author 飞花梦影
 * @date 2020-11-22 22:31:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "parameterize", args = java.sql.Statement.class) })
public class CustomPlugin implements Interceptor {

	/**
	 * invocation:拦截,拦截目标对象的目标方法的执行
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("CustomPlugin...intercept:" + invocation.getMethod());
		// 动态的改变一下sql运行的参数:以前1号用户,实际从数据库查询3号用户
		Object target = invocation.getTarget();
		// 对象结果根据Intercepts注解中的args获得
		// Object[] args = invocation.getArgs();
		// MappedStatement mappedStatement =(MappedStatement)args[0];
		// // 判断方法类型
		// if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
		// }
		System.out.println("当前拦截到的对象:" + target);
		// 拿到:StatementHandler==>ParameterHandler===>parameterObject
		// 拿到target的元数据
		MetaObject metaObject = SystemMetaObject.forObject(target);
		Object value = metaObject.getValue("parameterHandler.parameterObject");
		System.out.println("sql语句用的参数是:" + value);
		// 修改完sql语句要用的参数
		metaObject.setValue("parameterHandler.parameterObject", 11);
		// 执行目标方法
		Object proceed = invocation.proceed();
		// 返回执行后的返回值
		return proceed;
	}

	/**
	 * plugin:包装目标对象,为目标对象创建一个代理对象
	 */
	@Override
	public Object plugin(Object target) {
		// 借助Plugin的wrap方法来使用当前Interceptor包装成目标对象
		System.out.println("CustomPlugin...plugin:mybatis将要包装的对象" + target);
		Object wrap = Plugin.wrap(target, this);
		// 返回为当前target创建的动态代理
		return wrap;
	}

	/**
	 * setProperties:将插件注册时的property属性设置进来
	 */
	@Override
	public void setProperties(Properties properties) {
		System.out.println("插件配置的信息:" + properties);
	}
}