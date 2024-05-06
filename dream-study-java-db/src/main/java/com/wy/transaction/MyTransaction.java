package com.wy.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

/**
 * Spring事务说明
 * 
 * {@link PlatformTransactionManager},{@link AbstractPlatformTransactionManager}:是spring用于管理事务的真正对象
 * {@link TransactionDefinition}:定义事务的隔离级别,超时信息,传播行为,是否只读等信息
 * TransactionStatus:事务状态,用于记录在事务管理中,事务的状态的对象
 * spring进行事务管理时,需要根据事务定义信息进行事务管理,在事务管理过程中,会产生各种状态,记录在状态管理中
 * 
 * 事务的特性:原子性,隔离性,一致性,持久性
 * 
 * 为保证事务的一致性而产生的各种问题:
 * 
 * <pre>
 * 脏读:一个事务读到另一个事务未提交的数据
 * 不可重复读:一个事务读到另一个事务已经提交的update数据,导致一个事务中多次查询结果不一致
 * 虚读,幻读:一个事务读到另一个事务已经提交的insert数据,导致一个事务中多次查询结果不一致
 * </pre>
 * 
 * 为解决事务的一致性问题而产生的隔离机制:
 * 
 * <pre>
 * Read uncommited:未提交读,任何问题都解决不了,但是效率最高
 * Read commited:已提交读,解决赃读,oracle使用这种默认方式
 * Repeatable read:重复读,解决赃读和不可重复读,mysql默认使用的这种方式,且在新版的mysql中已经能解决所有问题<br>
 * Serialzable:解决所有问题,但是不可并发, 效率最低
 * {@link TransactionDefinition#ISOLATION_DEFAULT}:使用数据库自己默认的隔离机制<br>
 * {@link TransactionDefinition#ISOLATION_READ_UNCOMMITTED}:读未提交
 * {@link TransactionDefinition#ISOLATION_READ_COMMITTED}:已读提交
 * {@link TransactionDefinition#ISOLATION_REPEATABLE_READ}:可重复读
 * {@link TransactionDefinition#ISOLATION_SERIALIZABLE}:严格的一个一个读
 * </pre>
 * 
 * 事务的传播机制,解决事务的嵌套问题.例如A方法中调用了B方法,是否有多个事务可以使用,或者只使用一个事务:
 * 
 * <pre>
 * {@link TransactionDefinition#PROPAGATION_REQUIRED}:
 * 		默认传播行为,若A有事务,则使用A的事务,若没有则新建事务
 * {@link TransactionDefinition#PROPAGATION_SUPPORTS}:
 * 		如果A有事务,则使用A的事务,如果A当前没有事务,就不使用事务
 * {@link TransactionDefinition#PROPAGATION_MANDATORY}:
 * 		如果A有事务,则使用A的事务,如果A当前没有事务,就抛出异常
 * {@link TransactionDefinition#PROPAGATION_REQUIRES_NEW}:
 * 		新建事务,如果A当前存在事务,把当前事务挂起;如果A中有事务,则将A的事务挂起,新建一个事务,且只作用于B方法
 * {@link TransactionDefinition#PROPAGATION_NOT_SUPPORTED}:
 * 		以非事务方式执行操作,如果当前存在事务,就把当前事务挂起;如果A中有事务,则将A的事务挂起,不使用事务
 * {@link TransactionDefinition#PROPAGATION_NEVER}:
 * 		以非事务方式执行,如果当前存在事务,则抛出异常;如果A中有事务,直接报异常
 * {@link TransactionDefinition#PROPAGATION_NESTED}:
 * 		如果当前存在事务,则在嵌套事务内执行;如果当前没有事务,则进行与REQUIRED类似的操作
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2018-03-06 22:37:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyTransaction implements TransactionDefinition {
}