package com.wy.aop;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
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
 * {@link AbstractAutoProxyCreator}:BeanPostProcessor 实现,用AOP代理包装每个符合条件的bean,在调用bean本身之前委托给指定的拦截器
 * ->{@link AbstractAutoProxyCreator#postProcessAfterInitialization}:对初始化之后的bean进行AOP代理
 * -->{@link AbstractAutoProxyCreator#wrapIfNecessary}:判断Class是否需要代理,若需要,返回代理类以及相关切面
 * --->{@link BeanNameAutoProxyCreator#getAdvicesAndAdvisorsForBean}:根据beanName判断是否需要代理
 * ---->{@link AbstractAutoProxyCreator#createProxy}:将通用拦截,特殊拦截以及相关参数放入 ProxyFactory 中
 * ----->{@link ProxyFactory#getProxy()}:根据Class类型判断使用JDK动态代理或CGLIB代理,返回最终的代理对象
 * ------>{@link #JdkDynamicAopProxy#getProxy}:使用JDK动态代理生成代理类,非public
 * ------->{@link AopProxyUtils#completeProxiedInterfaces}:判断剔除不需要的代理类,返回所有需要代理的接口
 * ------>{@link #CglibAopProxy#getProxy}:使用CGLIB动态代理生成代理类,非public
 * ------->{@link #CglibAopProxy.DynamicAdvisedInterceptor#intercept}:AOP最终的代理对象的代理方法
 * 		this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,targetClass):该方法返回所有的拦截器
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-10-21 13:35:36
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyAspect {

}