package com.wy;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration.AutoConfiguredMapperScannerRegistrar;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.io.support.SpringFactoriesLoader;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wy.mybatis.CustomPlugin;

/**
 * MyBatis
 * 
 * SpringBoot整合MyBatis自动配置流程:
 * 
 * <pre>
 * 1.{@link SpringBootApplication}:由启动类上的该注解开始,引入自动配置类注解AutoConfigurationPackage
 * 2.{@link AutoConfigurationPackage}:让包中的类以及子包中的类能够被自动扫描到Spring容器中
 * ->2.1.{@link AutoConfigurationPackages.Registrar}:获取扫描的包路径.
 * 		即将 SpringBootApplication 标注的类所在包及子包里面所有组件扫描加载到Spring容器.
 * 		最终将被{@link #ConfigurationClassParser#parse}调用
 * 3.{@link EnableAutoConfiguration}:自动配置入口,引入自动导入类AutoConfigurationImportSelector
 * ->3.1{@link AutoConfigurationImportSelector#selectImports}:
 * 		由{@link AbstractApplicationContext#invokeBeanFactoryPostProcessors()}开始调用,
 * 		将所有{@link ImportSelector#selectImports()}接口方法中获得的组件的全类名添加到容器中.
 * 		会给容器中导入非常多的自动配置类,同时给容器中导入这个场景需要的所有组件,并配置好这些组件
 * ->3.2.{@link AutoConfigurationImportSelector#getCandidateConfigurations}:进入自动配置,加载所有自动配置类到spring容器中,
 * 		包括MyBatis挂件类{@link MybatisAutoConfiguration}
 * ->3.3.{@link AutoConfigurationImportSelector#getSpringFactoriesLoaderFactoryClass}:指定获得 EnableAutoConfiguration 类型
 * ->3.4.{@link SpringFactoriesLoader#loadSpringFactories}:从META-INF/spring.factories下获得所有自动配置类
 * 
 * 4.{@link MybatisAutoConfiguration#sqlSessionFactory()}:由自动配置加载,设置SqlSessionFactoryBean,
 * 		设置mybatis的{@link Configuration},最终注入SqlSessionFactory
 * 5.{@link SqlSessionFactoryBean#buildSqlSessionFactory()}:构建{@link SqlSessionFactory},在afterPropertiesSet()中调用,
 * 		同时解析全局配置文件以及业务mapper.xml文件存放到mybatis的{@link Configuration}对象中.
 * 		扫描到的Mapper通过动态代理产生Mapper的实现类存储到Spring容器中
 * ->5.1.{@link SqlSessionFactoryBuilder#build()}:将 Configuration 设置到 DefaultSqlSessionFactory 中并返回
 * ->5.2.{@link DefaultSqlSessionFactory}:默认实现的{@link SqlSessionFactory}
 * 6.{@link AutoConfiguredMapperScannerRegistrar}:从启动时获得的基础包扫描的所有类中找到含有{@link Mapper}修饰的接口
 * ->6.1.{@link MapperScan}:指定MyBatis扫描的包路径,该包下所有的接口都将被注入到spring容器中
 * ->6.2.{@link MapperScannerRegistrar}:注册由{@link MapperScan}扫描的接口,效果和AutoConfiguredMapperScannerRegistrar类似
 * 
 * 7.{@link SpringApplication#refreshContext()}:通过XML,注解构建SpringBean,AOP等实例的主要方法
 * 8.{@link AbstractApplicationContext#refresh()}:同步刷新上下文,初始化SpringBean,处理各种 BeanPostProcessor,AOP
 * 9.{@link AbstractApplicationContext#invokeBeanFactoryPostProcessors()}:获得包扫描时由注解标注的bean class,然后放入上下文.
 * 		激活各种{@link BeanFactory} 处理器,此时 BeanFactory 没有注册任何 BeanFactoryPostProcessor,此处相当于不做任何处理.
 * 		MyBatis就是在此处注入了{@link MapperScannerConfigurer},从而进一步解析MyBatis XML
 * 10.{@link #PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors()}:对所有的
 * 		{@link BeanDefinitionRegistryPostProcessor},手动以及通过配置文件方式注册的{@link BeanFactoryPostProcessor}
 * 		按照{@link PriorityOrdered},{@link Ordered},no Ordered三种方式分开处理,调用
 * 11.{@link #PostProcessorRegistrationDelegate#invokeBeanDefinitionRegistryPostProcessors()}:MyBatis注册bean定义
 * 12.{@link MapperScannerConfigurer#postProcessBeanDefinitionRegistry}:设置MyBatis相关参数,扫描包,注册扫描到的bean.
 * 		该方式是最关键的方法,将MyBatis中需要解析的接口注入到Spring上下文中.
 * 		真正的beanDefinition注入类,注入由{@link Mapper}以及{@link MapperScan}扫描到的接口
 * 13.{@link ClassPathBeanDefinitionScanner#scan}:扫描包,调用子类{@link ClassPathMapperScanner#doScan}
 * 14.{@link ClassPathMapperScanner#doScan}:先调用父类{@link ClassPathBeanDefinitionScanner#doScan}
 * ->14.1.{@link ClassPathBeanDefinitionScanner#doScan}:解析扫描启动类上的包路径以及子路径,获得 BeanDefinition
 * ->14.2.{@link ClassPathBeanDefinitionScanner#registerBeanDefinition}:注册 BeanDefinition
 * 15.{@link ClassPathMapperScanner#processBeanDefinitions}: MyBatis自定义处理 BeanDefinition,
 * 		将BeanClass设置为{@link MapperFactoryBean},替代了原先的Mapper接口类,之后访问Mapper接口由MapperFactoryBean创建对象
 * ->15.1.{@link MapperFactoryBean#getObject()}:在Spring容器中使用Mapper接口时,实际由该类的getObject()返回动态代理对象
 * </pre>
 * 
 * MyBatis进行数据库操作的主要类及流程:
 * 
 * <pre>
 * {@link SqlSessionFactoryBean}: 构建SqlSessionFactory,解析加载配置文件,业务mapper.xml文件存放到mybatis的{@link Configuration}中
 * {@link SqlSessionFactory}:MyBatis操作数据库起始接口,默认实现为DefaultSqlSessionFactory
 * {@link MapperScannerConfigurer}: 注入由{@link Mapper}和{@link MapperScan}扫描到的接口,生成代理beanDefinition到spring中
 * {@link DefaultSqlSessionFactory#openSession()}:开启会话,设置事务,执行SQL等
 * {@link DefaultSqlSession}:获取SqlSession对象,该对象包含Executor和Configuration
 * {@link SqlSession#getMapper()}:获取接口的代理对象,该对象中包含了各种Mapper接口,DefaultSqlSession和Executor
 * {@link MapperProxyFactory#newInstance(org.apache.ibatis.session.SqlSession)}:获取接口的代理对象
 * {@link MapperProxy#invoke()}:获取接口的代理对象,执行各种代理的增删改查
 * {@link MapperMethod#execute()}:最终的方法执行,根据SQL类型不同执行不同的策略
 * {@link MapperMethod.MethodSignature#convertArgsToSqlCommandParam()}:处理方法参数,将用户传递的参数转换为XML能识别的参数
 * {@link ParamNameResolver#getNamedParams(Object[])}:真正的参数转换,将用户参数处理后封装成XML能识别的类型
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
 * MyBatis相关对象:
 * 
 * <pre>
 * {@link Configuration}:MyBatis的主配置信息.
 * 		MyBatis在启动时,将Mapper配置信息,类型别名,TypeHandler等注册到该对象中,其他组件需要这些信息时,可以从该对象获取
 * {@link MappedStatement}:Mapper中的SQL配置信息,由{@link XMLStatementBuilder#parseStatementNode}构建.
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
 * MyBatis批量新增:
 * 
 * <pre>
 * 数据库在批量插入时都会有大小限制,此时需要将 SqlSessionFactory 中的 ExecutorType 设置为BATCH,
 * 该参数默认为SIMPLE,详见{@link Configuration#getDefaultExecutorType()}.
 * 但是当直接使用Mapper来调用方法时,该参数为全局配置,若直接配置为BATCH,则所有方法都是该类型,
 * 只有在使用SqlSessionFactory时进行设置单个提交才合适,故批量新增最好是单独提取出来
 * </pre>
 * 
 * MyBatis中的各种类型转换器
 * 
 * <pre>
 * {@link TypeHandler}:各种类型处理器最上层接口,特别是枚举类型以及一些基本类型
 * {@link EnumTypeHandler}:枚举类型的处理器,默认使用枚举的name属性
 * {@link EnumOrdinalTypeHandler}:也可以修改使用枚举的ordinal,需要在配置文件中修改
 * {@link BaseTypeHandler}:自定义类型转换器,继承该类或实现 TypeHandler.自定义类需要在配置文件中进行配置.
 * 		若只对某个枚举进行配置,需要在全局配置的xml中设置,此处为mybatis.xml;若针对所有枚举,在application.yml中配置
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
 * {@link DS}:MybatisPlus出品的多数据源工具,配置见application-dynamic.yml.方法注解优先于类上注解
 * 
 * <pre>
 * 支持数据源分组,适用于多种场景,纯粹多库,读写分离,一主多从,混合模式
 * 支持数据库敏感配置信息,加密 ENC()
 * 支持每个数据库独立初始化表结构schema和数据库database
 * 支持无数据源启动,支持懒加载数据源(需要的时候再创建连接)
 * 支持自定义注解,需继承DS(3.2.0+)
 * 提供并简化对Druid,HikariCp,BeeCp,Dbcp2的快速集成
 * 提供对Mybatis-Plus,Quartz,ShardingJdbc,P6sy,Jndi等组件的集成方案
 * 提供自定义数据源来源方案(如全从数据库加载)
 * 提供项目启动后动态增加移除数据源方案
 * 提供Mybatis环境下的纯读写分离方案
 * 提供使用spel动态参数解析数据源方案.内置spel,session,header,支持自定义
 * 支持多层数据源嵌套切换.ex:ServiceA >>> ServiceB >>> ServiceC
 * 提供基于seata的分布式事务方案
 * 提供本地多数据源事务方案
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