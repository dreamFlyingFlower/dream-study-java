package com.wy.annotation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 一些常用注解
 * 
 * 一些需要开启之后才可使用相关注解的注解,通常以Enable开头,有些已经在spring.factories中自动配置<br>
 * {@link EnableTransactionManagement}:开启事务,默认自动配置,不需要手动添加,可使{@link Transactional}生效
 * {@link EnableScheduling}:开启定时任务,可使{@link Scheduled}生效<br>
 * {@link EnableAsync}:开启异步任务,可使{@link Async}生效<br>
 * {@link EnableCaching}:开启缓存,可使{@link Cacheable}{@link CachePut}{@link CacheEvict}等缓存相关注解生效
 * 
 * 初始化相关注解:
 * 
 * <pre>
 * {@link Bean}:实例化该注解代表的方法返回值,并且纳入spring的上下文管理
 * {@link ConditionalOnBean}:仅仅在当前上下文中存在某个对象时,才会实例化一个Bean
 * {@link ConditionalOnClass}:某个class位于类路径上,才会实例化一个Bean
 * {@link ConditionalOnExpression}:当spel表达式为true的时候,才会实例化一个Bean
 * {@link ConditionalOnMissingBean}:仅仅在当前上下文中不存在某个对象时,才会实例化一个Bean
 * {@link ConditionalOnMissingClass}:某个class类路径上不存在的时候,才会实例化一个Bean
 * {@link ConditionalOnWebApplication}:是一个web应用,则使用springwebapplicationcontext
 * {@link ConditionalOnNotWebApplication}:不是web应用
 * {@link ConditionalOnProperty}:指定的属性要有一个明确的值
 * {@link CondiyionalOnResource}:classpath里有指定的资源
 * {@link Conditional}:当Spring内置的注解无法满足需求时,使用该注解可以控制更为复杂的配置条件
 * {@link DependsOn}:当初始化一个bean时,需要先初始化其他bean
 * {@link PostConstruct}:非spring注解,需要写在某个组件中,表示当该组件被初始化之前需要执行的方法
 * {@link PreDestroy}:非spring注解,需要写在某个组件中,表示当该组件被销毁之前需要执行的方法
 * {@link Primary}:当系统中需要配置多个具有相同类型的bean时,该注解标识的Bean可以被指定为默认使用的Bean
 * {@link Qualifier}:当系统中需要配置多个具有相同类型的bean时,若使用这些bean,依赖注入时无法判断,需要使用该注解进行分辨
 * </pre>
 * 
 * {@link Profile}:添加在类或方法上时表示在该环境下才有作用
 *
 * {@link JsonBackReference},{@link JsonManagedReference}:配对使用,通常用在父子关系中,比如树形结构.
 * JsonBackReference标注的属性在序列化(对象转json)时,会被忽略;JsonManagedReference标注的属性则会被序列化.
 * 在序列化时,JsonBackReference的作用相当于JsonIgnore,此时可以没有JsonManagedReference.
 * 但在反序列化(json转对象)时,如果没有JsonManagedReference,则不会自动注入JsonBackReference标注的属性(被忽略的父或子).
 * 如果有JsonManagedReference,则会自动注入JsonBackReference标注的属性.
 * 此时JsonManagedReference和JsonBackReference并不是在同一个属性上
 *
 * @author 飞花梦影
 * @date 2018-07-20 23:00:58
 * @git {@link https://github.com/mygodness100}
 */
public class S_Annotation {
}