package com.wy.annotation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * 一些常用注解
 * 
 * 一些需要开启之后才可使用相关注解的注解,通常以Enable开头,有些已经在spring.factories中自动配置<br>
 * {@link EnableTransactionManagement}:开启事务,默认自动配置,不需要手动添加,可使{@link Transactional}生效
 * {@link EnableScheduling}:开启定时任务,可使{@link Scheduled}生效<br>
 * {@link EnableAsync}:开启异步任务,可使{@link Async}生效<br>
 * {@link EnableCaching}:开启缓存,可使{@link Cacheable}{@link CachePut}{@link CacheEvict}等缓存相关注解生效
 * 
 * {@link Bean}:实例化该注解代表的方法返回值,并且纳入spring的上下文管理<br>
 * {@link ConditionalOnBean}:仅仅在当前上下文中存在某个对象时,才会实例化一个Bean<br>
 * {@link ConditionalOnClass}:某个class位于类路径上，才会实例化一个Bean<br>
 * {@link ConditionalOnExpression}:当spel表达式为true的时候,才会实例化一个Bean<br>
 * {@link ConditionalOnMissingBean}:仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean<br>
 * {@link ConditionalOnMissingClass}:某个class类路径上不存在的时候，才会实例化一个Bean<br>
 * {@link ConditionalOnWebApplication}:是一个web应用,则使用springwebapplicationcontext<br>
 * {@link ConditionalOnNotWebApplication}:不是web应用<br>
 * {@link ConditionalOnProperty}:指定的属性要有一个明确的值<br>
 * {@link CondiyionalOnResource}:classpath里有指定的资源<br>
 * {@link DependsOn}:当初始化一个bean时,需要先初始化其他bean<br>
 * {@link PostConstruct}:非spring注解,需要写在某个组件中,表示当该组件被初始化之前需要执行的方法<br>
 * {@link PreDestroy}:非spring注解,需要写在某个组件中,表示当该组件被销毁之前需要执行的方法<br>
 * 
 * {@link Profile}:添加在类或方法上时表示在该环境下才有作用
 *
 * @author 飞花梦影
 * @date 2018-07-20 23:00:58
 * @git {@link https://github.com/mygodness100}
 */
public class S_Annotation {
}