package com.wy.tracsaction;

import org.aopalliance.aop.Advice;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

/**
 * SpringBoot事务
 * 
 * 事务的特性:原子性,隔离性,一致性,持久性
 * 
 * 为保证事务的一致性而产生的各种问题:
 * 
 * <pre>
 * 脏读:一个事务读到另一个事务未提交的数据
 * 不可重复读:一个事务读到另一个事务已经提交的update数据,导致一个事务中多次查询结果不一致
 * 虚读,幻读:一个事务读到另一个事务已经提交的insert或delete数据,导致一个事务中多次查询结果数量不一致
 * </pre>
 * 
 * 为解决事务的一致性问题而产生的隔离机制:
 * 
 * <pre>
 * Read uncommited:未提交读,任何问题都解决不了,但是效率最高
 * Read commited:已提交读,解决赃读,oracle使用这种默认方式
 * Repeatable read:重复读,解决赃读和不可重复读,mysql默认使用的这种方式,且在新版的mysql中已经能解决所有问题
 * Serialzable:解决所有问题,但是不可并发, 效率最低
 * {@link TransactionDefinition#ISOLATION_DEFAULT}:使用数据库自己默认的隔离机制
 * {@link TransactionDefinition#ISOLATION_READ_UNCOMMITTED}:读未提交
 * {@link TransactionDefinition#ISOLATION_READ_COMMITTED}:已读提交
 * {@link TransactionDefinition#ISOLATION_REPEATABLE_READ}:可重复读
 * {@link TransactionDefinition#ISOLATION_SERIALIZABLE}:严格的一个一个读
 * </pre>
 * 
 * 事务的传播机制,解决事务的嵌套问题.例如A()中调用了B(),是否有多个事务可以使用,或者只使用一个事务,或不使用事务
 * 
 * <pre>
 * {@link TransactionDefinition#PROPAGATION_REQUIRED}:默认,若A有事务,则使用A的事务;若没有则新建事务
 * {@link TransactionDefinition#PROPAGATION_SUPPORTS}:若A有事务,则使用A的事务;若A没有事务,就不使用事务
 * {@link TransactionDefinition#PROPAGATION_MANDATORY}:若A有事务,则使用A的事务;若A没有事务,就抛出异常
 * {@link TransactionDefinition#PROPAGATION_REQUIRES_NEW}:若A有事务,将A事务挂起,新建一个事务,且只作用于B
 * {@link TransactionDefinition#PROPAGATION_NOT_SUPPORTED}:非事务方式执行操作,不管A,B有事务,都将事务挂起执行
 * {@link TransactionDefinition#PROPAGATION_NEVER}:非事务方式执行,若A,B中任何一个有事务,直接抛异常
 * {@link TransactionDefinition#PROPAGATION_NESTED}:若存在事务,则在嵌套事务内执行;若没有事务,则与REQUIRED类似
 * </pre>
 * 
 * Spring事务动态代理原理:
 * 
 * <pre>
 * {@link Transactional}:定义代理植入点,标识方法需要被代理,同时携带事务管理需要的一些属性信息
 * {@link TransactionDefinition}:定义事务的隔离级别,超时信息,传播行为,是否只读等信息
 * {@link TransactionStatus}:事务状态,根据事务定义信息进行事务管理,记录事务管理中的事务状态的对象
 * {@link AnnotationAwareAspectJAutoProxyCreator#postProcessAfterInitialization}:{@link BeanPostProcessor}的实现类,
 * 		主要是判断事务代理的植入点,返回一个代理对象给Spring容器.
 * {@link BeanFactoryTransactionAttributeSourceAdvisor}:在配置好注解驱动方式的事务管理之后,
 * 		Spring会在IOC容器创建一个该实例.这个实例可以看作是一个切点,在判断一个bean在初始化过程中是否需要创建代理对象时,
 * 		都需要验证一次 BeanFactoryTransactionAttributeSourceAdvisor 是否适用这个bean的切点.
 * 		如果适用于这个切点,就需要创建代理对象,并且把 BeanFactoryTransactionAttributeSourceAdvisor 实例注入到代理对象中
 * {@link AopUtils#findAdvisorsThatCanApply}:判断切面是否适用当前bean,可以在这个地方断点分析调用堆栈,
 * AopUtils#findAdvisorsThatCanApply一致调用,最终通过以下代码判断是否适用切点
 * {@link AbstractFallbackTransactionAttributeSource#computeTransactionAttribute}:targetClass就是目标class
 * 	{@link SpringTransactionAnnotationParser#parseTransactionAnnotation}:分析方法是否被 Transactional 标注,
 * 		如果有,BeanFactoryTransactionAttributeSourceAdvisor适配当前bean,进行代理,并注入切入点
 * {@link #CglibAopProxy.DynamicAdvisedInterceptor#intercept}:AOP最终的代理对象的代理方法
 * 		this.advised.getInterceptorsAndDynamicInterceptionAdvice():该方法返回 TransactionInterceptor
 * 		new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed():
 * 			最终调用{@link TransactionInterceptor#invoke},并且把CglibMethodInvocation注入到invoke()
 * {@link TransactionInterceptor}:事务拦截器,实现了{@link MethodInterceptor},{@link Advice},在SpringBoot启动时注入
 * {@link TransactionInterceptor#invoke}:AOP切面最终调用的执行方法.
 * 		从调用链可以看到CglibMethodInvocation是包装了目标对象的方法调用的所有信息,因此,在该方法里面也可以调用目标方法,
 * 		并且还可以实现类似@Around的逻辑,在目标方法调用前后继续注入一些其他逻辑,比如事务管理逻辑
 * {@link TransactionAspectSupport#invokeWithinTransaction}:事务最终的调用方法,对开启了事务的方法和类进行主要操作
 * 		检查事务的传播机制,隔离级别,根据事务相关属性获得{@link TransactionManager},执行代理方法.
 * 		根据结果是否抛出异常,隔离机制等决定是否提交事务,回滚等
 * ->{@link TransactionAspectSupport#createTransactionIfNecessary}:开启事务
 * ->{@link TransactionAspectSupport#completeTransactionAfterThrowing}:回滚事务
 * ->{@link TransactionAspectSupport#commitTransactionAfterReturning}:提交事务
 * {@link PlatformTransactionManager},{@link AbstractPlatformTransactionManager}:Spring用于管理事务的真正对象
 * {@link AbstractFallbackTransactionAttributeSource#getTransactionAttribute()}:事务回滚主要方法,非public不回滚
 * </pre>
 * 
 * Spring事务失效:
 * 
 * <pre>
 * 1.当A()和B()在同一个类中,且A()调用B()时.由于Spring事务采用动态代理,当A()使用了事务时,
 * 		若B()开启了新事务,此时A()中调用B()使用的是this.B(),而不是由Spring的动态代理调用的B(),此时B()的事务不生效
 * 2.开启了多个数据库的事务
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-10-21 13:48:02
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyTransaction {

	@Autowired
	private ApplicationContext applicationContext;

	@Transactional(rollbackFor = Exception.class)
	public void test01() {
		// 当test01有事务:若此时发生了异常,且异常被抛出,不管test02上是否有事务,事务仍然生效,test01和test02都回滚
		test02();
		try {
			// 若此时发生了异常,但是异常被捕获,事务不生效
			test02();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 当test01没有事务,test02有事务:若此时发生异常,由于是由this直接调用,不走AOP的动态代理,无法捕获异常,事务不生效
		test02();
		// 当test01没有事务,test02有事务:使用AopContext调用test02,此时若发生异常,可被AOP捕获,事务生效
		MyTransaction myTransaction = (MyTransaction) AopContext.currentProxy();
		myTransaction.test02();
		// 使用ApplicationContext获得代理对象,事务生效
		MyTransaction myTransaction2 = applicationContext.getBean(MyTransaction.class);
		myTransaction2.test02();
	}

	@Transactional(rollbackFor = Exception.class)
	public void test02() {

	}
}