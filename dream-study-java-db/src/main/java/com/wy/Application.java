package com.wy;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.io.support.SpringFactoriesLoader;

import com.wy.mybatis.CustomPlugin;

/**
 * MyBatis
 * 
 * MyBatis运行流程:
 * 
 * <pre>
 * {@link MybatisAutoConfiguration#sqlSessionFactory()}:自动配置
 * {@link SqlSessionFactoryBean#buildSqlSessionFactory()}:构建工厂
 * {@link DefaultSqlSessionFactory}:获取{@link SqlSessionFactory},解析XML以及全局配置文件等放到{@link Configuration}中
 * {@link DefaultSqlSessionFactory#openSession()}:开启会话
 * {@link DefaultSqlSession}:获取SqlSession对象,该对象包含Executor和Configuration
 * {@link SqlSession#getMapper()}:获取接口的代理对象,该对象中包含了各种Mapper接口,DefaultSqlSession和Executor
 * {@link MapperProxyFactory#newInstance(org.apache.ibatis.session.SqlSession)}:获取接口的代理对象
 * {@link MapperProxy#invoke()}:获取接口的代理对象,执行各种代理的增删改查
 * 
 * 调用 DefaultSqlSession 的增删改查(Executor)
 * ->创建StatementHandler,ParameterHandler和ResultSetHandler
 * ->调用StatementHandler预编译参数以及设置参数值
 * ->使用ParameterHandler来给sql设置参数
 * ->调用StatementHandler的增删改查方法
 * ->ResultSetHandler封装结果
 * 
 * 1.根据配置文件创建SQLSessionFactory:
 * 2.返回SqlSession的实现类DefaultSqlSession对象,他里面包含了Executor和Configuration,Executor会在这一步被创建
 * 3.getMapper返回接口的代理对象,包含了SqlSession对象
 * 4.进行查询
 * 图文解析:docs/db/MyBatis01.png->MyBatis04.png
 * </pre>
 * 
 * MyBatis主要对象:
 * 
 * <pre>
 * {@link Configuration}:MyBatis的主配置信息.
 * 		MyBatis在启动时,将Mapper配置信息,类型别名,TypeHandler等注册到该对象中,其他组件需要这些信息时,可以从该对象获取
 * {@link MappedStatement}:Mapper中的SQL配置信息.
 * 		封装Mapper XML配置文件中<insert><delete><update><select>等标签或{@link Select}{@link Update}等注解配置信息
 * {@link SqlSession}:MyBatis提供的面向用户的API,和数据库交互时的会话对象,用于完成数据库的增删改查功能.
 * 		SqlSession是Executor组件的外观,目的是对外提供易于理解和使用的数据库操作接口
 * {@link Executor}:MyBatis的SQL执行器,MyBatis中对数据库所有的增删改查操作都是由Executor组件完成的
 * {@link StatementHandler}:封装了对{@link Statement}的操作,比如为 Statement 设置参数,调用 Statement 和数据库进行交互等
 * {@link ParameterHandler}:MyBatis使用的 Statement 为{@link CallableStatement}和{@link PreparedStatement}时,
 * 		ParameterHandler 用于为Statement对象参数占位符设置值
 * {@link ResultSetHandler}:对{@link ResultSet}进行封装,当执行SELECT时,ResultSetHandler 将查询结果转换成Java对象
 * {@link TypeHandler}:类型处理器,处理Java类型与JDBC类型之间的映射.
 * </pre>
 * 
 * {@link Interceptor}:MyBatis拦截设置
 * 
 * <pre>
 * {@link Executor},{@link ParameterHandler},{@link ResultSetHandler},{@link StatementHandler}:
 * 		在执行之前都有interceptorChain.pluginAll()可以拦截,实现该接口即可
 * 实现了 Interceptor 的类需要在全局配置文件中进行注册(mybatis或application.yml)
 * Interceptor 中若需要提供额外的参数,可以在实现类中重写setProperties(),详见{@link CustomPlugin}
 * </pre>
 * 
 * 批量新增:数据库在批量插入时都会有大小限制,此时需要将 SqlSessionFactory 中的 ExecutorType 设置为BATCH,
 * 该参数默认为SIMPLE,详见{@link Configuration#getDefaultExecutorType()}.
 * 但是当直接使用Mapper来调用方法时,该参数为全局配置,若直接配置为BATCH,则所有方法都是该类型,
 * 只有在使用SqlSessionFactory时进行设置单个提交才合适,故批量新增最好是单独提取出来
 * 
 * MyBatis中的各种类型转换器
 * 
 * <pre>
 * {@link TypeHandler}:各种类型处理器最上层接口,特别是枚举类型以及一些基本类型
 * {@link EnumTypeHandler}:枚举类型的处理器,默认使用枚举的name属性
 * {@link EnumOrdinalTypeHandler}:也可以修改使用枚举的ordinal
 * {@link BaseTypeHandler}:自定义类型转换器,继承该类或实现 TypeHandler.自定义类需要在配置文件中进行配置.
 * 		若只对某个枚举进行配置,需要在全局配置的xml中设置,此处为mybatis.xml;若针对所有枚举,在application.yml中配置
 * </pre>
 * 
 * SpringBoot整合MyBatis核心流程:
 * 
 * <pre>
 * {@link AutoConfigurationPackage}:让包中的类以及子包中的类能够被自动扫描到Spring容器中
 * ->{@link AutoConfigurationPackages.Registrar}:获取扫描的包路径.
 * 		即将 SpringBootApplication 标注的类所在包及子包里面所有组件扫描加载到Spring容器.
 * 		最终将被{@link #ConfigurationClassParser#parse}调用
 * {@link AutoConfigurationImportSelector#selectImports}:自动配置.获得所有需要导入的组件的全类名,并添加到容器中.
 * 		会给容器中导入非常多的自动配置类,给容器中导入这个场景需要的所有组件,并配置好这些组件
 * {@link AutoConfigurationImportSelector#getSpringFactoriesLoaderFactoryClass}:指定获得 EnableAutoConfiguration 类型
 * ->{@link SpringFactoriesLoader#loadSpringFactories}:从META-INF/spring.factories下获得所有自动配置类
 * 
 * {@link SpringApplication#refreshContext()}:通过XML,注解构建SpringBean,AOP等实例的主要方法
 * ->{@link AbstractApplicationContext#refresh()}:同步刷新上下文,初始化SpringBean,处理各种 BeanPostProcessor,AOP
 * ->{@link AbstractApplicationContext#invokeBeanFactoryPostProcessors()}:获得包扫描时由注解标注的bean class,然后放入上下文.
 * 		激活各种{@link BeanFactory} 处理器,此时 BeanFactory 没有注册任何 BeanFactoryPostProcessor,此处相当于不做任何处理.
 * 		MyBatis就是在此处注入了{@link MapperScannerConfigurer},从而进一步解析MyBatis XML
 * -->{@link #PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors()}:对所有的
 * 		{@link BeanDefinitionRegistryPostProcessor},手动以及通过配置文件方式注册的{@link BeanFactoryPostProcessor}
 * 		按照{@link PriorityOrdered},{@link Ordered},no Ordered三种方式分开处理,调用
 * -->{@link #PostProcessorRegistrationDelegate#invokeBeanDefinitionRegistryPostProcessors()}:MyBatis注册bean定义
 * --->{@link MapperScannerConfigurer#postProcessBeanDefinitionRegistry}:设置MyBatis相关参数,扫描包,注册扫描到的bean.
 * 		该方式是最关键的方法,将MyBatis中需要解析的接口注入到Spring上下文中
 * ---->{@link ClassPathBeanDefinitionScanner#scan}:扫描包
 * ----->{@link ClassPathMapperScanner#doScan}:子类实现
 * ------>{@link ClassPathBeanDefinitionScanner#doScan}:解析包路径以及bean class,获得 BeanDefinition
 * ------>{@link ClassPathBeanDefinitionScanner#registerBeanDefinition}:注册 BeanDefinition
 * ----->{@link ClassPathMapperScanner#processBeanDefinitions}:MyBatis自定义处理 BeanDefinition,
 * 		将BeanClass设置为{@link MapperFactoryBean},之后的Mapper接口由该类来创建对象
 * ------>{@link MapperFactoryBean#getObject()}:在Spring上下文中注入的Mapper接口对象由该方法创建动态代理
 * </pre>
 * 
 * 不停机分库分表数据迁移
 * 
 * <pre>
 * 1.利用MySQL+Canal做增量数据同步,利用分库分表中间件,将数据路由到对应的新表中
 * 2.利用分库分表中间件,全量数据导入到对应的新表中
 * 3.通过单表数据和分库分表数据两两比较,更新不匹配的数据到新表中
 * 4.数据稳定后,将单表的配置切换到分库分表配置上
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-09-25 23:11:18
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}