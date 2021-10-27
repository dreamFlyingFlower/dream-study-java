package com.wy.aop;

import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration.EnableTransactionManagementConfiguration.CglibAutoProxyConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration.EnableTransactionManagementConfiguration.JdkDynamicAutoProxyConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring Aop
 * 
 * {@link EnableAspectJAutoProxy}:该注解指定代理使用的是JDK动态代理还是CGLIB,默认false,使用JDK动态代理
 * {@link #JdkDynamicAopProxy}:当使用JDK动态代理时的代理处理类,非public
 * {@link #CglibAopProxy}:当使用CGLIB动态代理时的代理处理类,非public
 * {@link TransactionAutoConfiguration}:事务自动配置类,会根据配置决定是使用JDK动态代理,还是CGLIB动态代理
 * {@link JdkDynamicAutoProxyConfiguration}:当spring.aop.proxy-target-class为false,使用JDK动态代理,默认代理
 * {@link CglibAutoProxyConfiguration}:当spring.aop.proxy-target-class为true时,使用CGLIB动态代理
 * 
 * AOP原理:
 * 
 * <pre>
 * {@link #CglibAopProxy.DynamicAdvisedInterceptor#intercept}:AOP最终的代理对象的代理方法
 * 		this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,targetClass):该方法返回所有的拦截器
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-10-21 13:35:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyAspect {

}