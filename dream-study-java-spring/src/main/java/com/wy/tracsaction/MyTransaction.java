package com.wy.tracsaction;

import org.aopalliance.aop.Advice;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

/**
 * SpringBoot事务
 * 
 * 事务的特性:原子性,隔离性,一致性,持久性
 * 
 * 为保证事务的一致性而产生的各种问题:<br>
 * 脏读:一个事务读到另一个事务未提交的数据<br>
 * 不可重复读:一个事务读到另一个事务已经提交的update数据,导致一个事务中多次查询结果不一致
 * 虚读,幻读:一个事务读到另一个事务已经提交的insert数据,导致一个事务中多次查询结果不一致
 * 
 * 为解决事务的一致性问题而产生的隔离机制
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
 * 事务的传播机制,解决事务的嵌套问题.例如A方法中调用了B方法,是否有多个事务可以使用,或者只使用一个事务,或不使用事务
 * 
 * <pre>
 * {@link TransactionDefinition#PROPAGATION_REQUIRED}:默认传播行为,若A有事务,则使用A的事务,若没有则新建事务
 * {@link TransactionDefinition#PROPAGATION_SUPPORTS}:如果A有事务,则使用A的事务,如果A没有事务,就不使用事务
 * {@link TransactionDefinition#PROPAGATION_MANDATORY}:如果A有事务,则使用A的事务,如果A没有事务,就抛出异常
 * {@link TransactionDefinition#PROPAGATION_REQUIRES_NEW}:若A有事务,将A事务挂起,新建一个事务,且只作用于B
 * {@link TransactionDefinition#PROPAGATION_NOT_SUPPORTED}:非事务方式执行操作,不管A,B有事务,都将事务挂起执行
 * {@link TransactionDefinition#PROPAGATION_NEVER}:非事务方式执行,若A,B中任何一个有事务,直接抛异常
 * {@link TransactionDefinition#PROPAGATION_NESTED}:若存在事务,则在嵌套事务内执行;若没有事务,则与REQUIRED类似
 * </pre>
 * 
 * SpringBoot事务流程原理
 * 
 * <pre>
 * {@link TransactionInterceptor}:事务拦截器,实现了{@link MethodInterceptor},{@link Advice},在SpringBoot启动时注入
 * {@link TransactionAspectSupport}:事务切面拦截器,对开启了事务的方法和类进行主要操作的类
 * {@link PlatformTransactionManager},{@link AbstractPlatformTransactionManager}:是spring用于管理事务的真正对象
 * {@link TransactionDefinition}:定义事务的隔离级别,超时信息,传播行为,是否只读等信息
 * {@link TransactionStatus}:事务状态,根据事务定义信息进行事务管理,记录事务管理中的事务状态的对象
 * {@link AbstractFallbackTransactionAttributeSource#getTransactionAttribute()}:事务回滚主要方法,非public不回滚
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-10-21 13:48:02
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyTransaction {

}