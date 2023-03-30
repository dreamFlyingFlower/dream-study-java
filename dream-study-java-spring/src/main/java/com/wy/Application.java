package com.wy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.annotation.HandlesTypes;

import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.loader.JarLauncher;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.wy.extension.SelfApplicationContextAwareProcessor;
import com.wy.extension.SelfApplicationContextInitializer;
import com.wy.extension.SelfBeanDefinitionRegistryPostProcessor;
import com.wy.extension.SelfBeanFactoryAware;
import com.wy.extension.SelfBeanFactoryPostProcessor;
import com.wy.extension.SelfBeanNameAware;
import com.wy.extension.SelfDisposableBean;
import com.wy.extension.SelfFactoryBean;
import com.wy.extension.SelfInitializingBean;
import com.wy.extension.SelfInstantiationAwareBeanPostProcessor;
import com.wy.extension.SelfSmartInitializingSingleton;
import com.wy.extension.SelfSmartInstantiationAwareBeanPostProcessor;
import com.wy.runner.SelfCommandLineRunner;

/**
 * SpringBoot学习:初始化initialize,listener,自动配置,配置文件.
 * 
 * SPEL语法:{@link https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions}
 * 
 * 若要创建一个starter,通常情况下应该是2个项目:一个只有spring.factories,引入真正的项目;另一个是真正的项目
 * 
 * Spring读取配置文件的顺序,先从外部读取,再读取内部,高优先级会覆盖低优先级属性,详见官方文档:<br>
 * 外部:和jar包同级目录的config目录下的配置->和jar同级目录的配置
 * 内部:项目根目录下的config目录下的配置->项目根目录下的配置->classpath:/config->classpath:/
 * 
 * 在启动jar时指定的配置优先级最高,会覆盖所有其他同名配置.<br>
 * 启动jar时,在启动参数上指定读取的配置文件和配置文件路径:<br>
 * 指定配置文件:java -jar test.jar --spring.profiles.active=dev,config<br>
 * 指定配置文件目录:java -jar test.jar --spring.config.location=/config
 * 
 * {@link SpringApplicationRunListener}:在调用run()时调用,所有实现该接口的类都必须添加一个构造,
 * 且该构造的参数类型固定,详见其他实现类.若不添加构造,启动报错.<br>
 * 实现该接口的类使用@Configuration或@Component等注解无法注入到Spring上下文中,
 * 只能通过在resources目录下的META-INF/spring.factories添加相应配置才能生效
 * 
 * {@link ApplicationContextInitializer}:在调用run()时调用,在IOC上下文环境准备完成之前调用
 * 该接口的实现类同{@link SpringApplicationRunListener}一样,只能在spring.factories中添加才能生效
 * 
 * 自动配置类在spring扫描不到的情况下,仍然能注入到spring上下文中,同样是通过spring.factories加载
 * 
 * 若开启了actuator的shutdown配置,则可以使用post方式远程关闭应用:curl -X POST
 * ip:port/actuator/shutdown
 * 
 * Springboot启动的大概流程:
 * 
 * <pre>
 * 1.{@link BeanDefinitionReader}: 读取配置文件或注解修饰的类构建bean实例
 * 2.{@link BeanFactory}: 构建bean实例容器
 * 3.{@link Environment}: 各种环境,包括配置文件,系统环境变量等
 * 4.{@link BeanFactoryPostProcessor}: BeanFactory实例化的前置操作和后置操作
 * 5.{@link BeanPostProcessor}: Bean实例化后的初始化前置和后置操作
 * 6.{@link FactoryBean}: 生成各种bean的工厂,类似于 ProxyFactoryBean
 * </pre>
 * 
 * Spring Bean的初始化过程涉及如下几个过程:
 * 
 * <pre>
 * 1.Bean实例的属性填充
 * ->1.1.注入普通属性,String,int或存储基本类型的集合时,直接通过set方法的反射设置进去
 * ->1.2.注入单向对象(不互相引用)引用属性时,从容器中getBean()获取后通过set方法反射设置进去;
 * 		如果容器中没有,则先创建被注入对象Bean实例(完成整个生命周期)后,在进行注入操作作
 * ->1.3.注入双向对象(相互引用)引用属性时,就比较复杂了,涉及了循环引用(循环依赖)问题
 * 2.Aware接口属性注入
 * ->2.1.{@link BeanFactoryAware},{@link BeanNameAware},{@link ApplicationContextAware}等
 * 3.{@link BeanPostProcessor#postProcessBeforeInitialization()}回调
 * 4.InitializingBean接口的初始化方法回调
 * 5.自定义初始化方法init回调,被{@link PostConstruct}修饰的初始化方法
 * 6.{@link BeanPostProcessor#postProcessAfterInitialization()} 回调
 * </pre>
 * 
 * SpringBoot启动流程-SpringApplication:
 * 
 * <pre>
 * 1.{@link SpringApplication#run(Class, String...)}:推断启动类,判断应用环境(SERVLET,REACTIVE,NONE)
 * 2.{@link SpringApplication#setInitializers}:加载所有META-INF/spring.factories中的{@link ApplicationContextInitializer}
 * 3.{@link SpringApplication#setListeners}:加载所有META-INF/spring.factories中的{@link ApplicationListener}
 * 4.{@link SpringApplication#deduceMainApplicationClass}:推断main所在的类
 * 
 * 5.{@link SpringApplication#run(String...)}:开始执行run()
 * 6.{@link SpringApplication#createBootstrapContext()}:创建启动上下文context
 * 7.{@link SpringApplication#configureHeadlessProperty}:设置java.awt.headless系统变量
 * 8.{@link SpringApplication#getRunListeners}:加载所有META-INF/spring.factories中的{@link SpringApplicationRunListener}
 * 9.执行所有的{@link SpringApplicationRunListener#starting()}
 * 10.{@link DefaultApplicationArguments}:实例化ApplicationArguments对象
 * 11.{@link SpringApplication#prepareEnvironment}:创建Environment
 * 12.{@link SpringApplication#configureEnvironment}:配置Enviroment,主要是把run()的参数配置到Environment中
 * 13.执行所有{@link SpringApplicationRunListener#environmentPrepared()}
 * 14.{@link SpringApplication#configureIgnoreBeanInfo}:设置需要忽略的环境变量
 * 15.{@link SpringApplication#printBanner}:设置日志打印Banner
 * 16.{@link SpringApplication#createApplicationContext}:根据应用类型加载不同context,设置不同的ConfigurableApplicationContext
 * ->16.1.当webApplicationType为SERVLET时,context为 AnnotationConfigServletWebServerApplicationContext;
 * 		当webApplicationType为REACTIVE时,context为 AnnotationConfigReactiveWebServerApplicationContext
 * 17.{@link SpringApplication#getSpringFactoriesInstances}:加载自定义的异常报告
 * 18.{@link SpringApplication#prepareContext}:预加载ConfigurableApplicationContext
 * 19.{@link SpringApplication#postProcessApplicationContext}:加载beanNameGenerator,resourceLoade,raddConversionService
 * 20.{@link SpringApplication#applyInitializers}:回调所有的{@link ApplicationContextInitializer#initialize}
 * 21.执行所有的{@link SpringApplicationRunListener#contextPrepared()}
 * 22.{@link SpringApplication#load}:设置beanNameGenerator,resourceLoade,raddConversionService
 * 23.执行所有的{@link SpringApplicationRunListener#contextLoaded()}
 * 24.{@link SpringApplication#refreshContext}:调用context的registerShutdownHook(),执行context的refresh(),
 * 		根据不同环境执行不同的refresh(),最终调用{@link AbstractApplicationContext#refresh()},注册各种Bean以及Spring组件
 * 25.{@link SpringApplication#afterRefresh}:用户可自定义加载完成的方法
 * 26.执行所有的{@link SpringApplicationRunListener#started()}
 * 27.{@link SpringApplication#callRunners}:回调,获取容器中所有的{@link ApplicationRunner},{@link CommandLineRunner},依次调用
 * 28.执行所有的{@link SpringApplicationRunListener#running()}
 * 29.返回{@link ConfigurableApplicationContext}
 * </pre>
 * 
 * SpringBoot启动流程-{@link SpringBootApplication}注解:
 * 
 * <pre>
 * {@link AutoConfigurationPackage}:让包中的类以及子包中的类能够被自动扫描到Spring容器中
 * ->{@link AutoConfigurationPackages.Registrar}:获取扫描的包路径.
 * 		即将 SpringBootApplication 标注的类所在包及子包里面所有组件扫描加载到Spring容器.
 * 		最终将被{@link #ConfigurationClassParser#parse}调用
 * {@link AutoConfigurationImportSelector#selectImports}:自动配置.获得所有需要导入的组件的全类名,并添加到容器中.
 * 		会给容器中导入非常多的自动配置类,给容器中导入这个场景需要的所有组件,并配置好这些组件
 * ->{@link AutoConfigurationImportSelector#getAutoConfigurationEntry}
 * ->{@link AutoConfigurationImportSelector#getCandidateConfigurations}
 * ->{@link AutoConfigurationImportSelector#getSpringFactoriesLoaderFactoryClass}:指定获得 EnableAutoConfiguration 类型
 * -->{@link SpringFactoriesLoader#loadSpringFactories}:从META-INF/spring.factories下获得所有自动配置类
 * 
 * {@link SpringApplication#SpringApplication(ResourceLoader, Class...)}:从META-INF/spring.factories中加载如下自动配置类,
 * 		{@link ApplicationContextInitializer},{@link ApplicationListener}等
 * </pre>
 * 
 * SpringBoot启动流程--{@link AbstractApplicationContext#refreshContext()}:
 * 
 * <pre>
 * {@link SpringApplication#refreshContext()}:通过XML,注解构建SpringBean,AOP等实例的主要方法
 * ->{@link AbstractApplicationContext#refresh()}:同步刷新上下文,初始化SpringBean,处理各种 BeanPostProcessor,AOP
 * ->{@link AbstractApplicationContext#prepareRefresh()}:预刷新,初始化配置文件,读取{@link Environment}相关参数
 * --> initPropertySources()初始化一些属性设置;子类自定义个性化的属性设置方法
 * --> getEnvironment().validateRequiredProperties();检验属性的合法等
 * --> earlyApplicationEvents= new LinkedHashSet<ApplicationEvent>();保存容器中的-些早期的事件
 * 
 * ->{@link AbstractApplicationContext#obtainFreshBeanFactory()}:告诉子类去刷新内部的beanFactory,获得刷新后的beanFactory,
 * 		最终返回{@link DefaultListableBeanFactory},默认的beanFactory.主要就是根据注解,XML等创建 BeanDefinition
 * 
 * ->{@link AbstractApplicationContext#prepareBeanFactory()}:预处理beanFactory,为在上下文中使用.
 * 		设置beanFactory的类加载器,支持表达式解析器;
 * 		添加部分BeanPostProcessor[ApplicationContextAwareProcessor];
 * 		设置忽略的自动装配的接口EnvironmentAware,EmbeddedValueResolverAware等;
 * 		注册可以解析的自动装配,能直接在任何组件中注入:BeanFactory,ResourceLoader,ApplicationEventPublisher,Application;
 * 		添加BeanPostProcessor[ApplicationListenerDetector];
 * 		添加编译时的AspectJ;
 * 		给BeanFactory中注册一些公共组件:environment[ConfigurableEnvironment],systemProperties,systemEnvironment
 * 
 * ->{@link AbstractApplicationContext#postProcessBeanFactory()}:对BeanFactory进一步处理,由子类实现该方法,Spring不做任何处理
 * 
 * ->{@link AbstractApplicationContext#invokeBeanFactoryPostProcessors()}:BeanFactory后置处理器,在BeanFactory初始化之后执行.
 * 		激活各种BeanFactory处理器,目前BeanFactory没有注册任何BeanFactoryPostProcessor,此处相当于不做任何处理.
 * 		MyBatis就是在此处注入了#MapperScannerConfigurer,从而进一步解析MyBatis XML.
 * -->	在此处主要是两个接口: BeanFactoryPostProcessor,BeanDefinitionRegistryPostProcessor(BeanFactoryPostProcessor的子接口)
 * -->	先执行BeanDefinitionRegistryPostProcessor:
 * --->1.获取所有的BeanDefinitionRegistryPostProcessor;
 * --->2.先执行实现了{@link PriorityOrdered}的BeanDefinitionRegistryPostProcessor
 * --->3.再执行实现了{@link Ordered}的BeanDefinitionRegistryPostProcessor
 * --->4.最后执行没有实现PriorityOrdered和Ordered的BeanDefinitionRegistryPostProcessors
 * -->再执行BeanFactoryPostProcessor:过程和执行BeanDefinitionRegistryPostProcessor相同
 * 
 * ->{@link AbstractApplicationContext#registerBeanPostProcessors()}:注册 BeanPostProcessor 后置处理器,拦截bean的创建.
 * 		如果没有BeanProcessors,不做任何处理.当前只做注册,实际调用的是{@link BeanFactory#getBean()}.
 * 		不同接口类型的BeanPostProcessor,在Bean创建前后的执行时机不一样,主要是以下几个接口:
 * 		{@link DestructionAwareBeanPostProcessor}:
 * 		{@link InstantiationAwareBeanPostProcessor}:
 * 		{@link SmartInstantiationAwareBeanPostProcessor}:
 * 		{@link MergedBeanDefinitionPostProcessor}:[internalPostProcessors]
 * 
 * ->{@link AbstractApplicationContext#initMessageSource()}:注册MessageaSource,国际化语言处理
 * 
 * ->{@link AbstractApplicationContext#initApplicationEventMulticaster()}:注册applicationEventMulticaster,应用广播消息,发布监听
 * 
 * ->{@link AbstractApplicationContext#registerListeners()}:在所有bena中查找listener bean并注册到消息广播中
 * 
 * ->{@link AbstractApplicationContext#finishBeanFactoryInitialization()}:初始化所有剩下的非延迟初始化的单例bean对象实例,
 * 		Bean的IOC,DI,AOP都是在该方法中调用
 * -->{@link AbstractBeanFactory#doGetBean()}:执行获得bean实例的方法
 * -->{@link AbstractBeanFactory#getSingleton()}:获得bean实例的单例对象
 * --->{@link DefaultSingletonBeanRegistry#getSingleton()}:获得bean实例的单例对象的实际操作类,同时解决循环依赖的问题
 * <code>
 * 先从一级缓存中查看是否存在bean对象
 *	Object singletonObject = this.singletonObjects.get(beanName);
 *	如果不存在且该bean正在创建中,则去查看二级缓存
 *	if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
 *		查看二级缓存,如果不存在且允许循环依赖,则查看三级缓存
 *		singletonObject = this.earlySingletonObjects.get(beanName);
 *		if (singletonObject == null && allowEarlyReference) {
 *			synchronized (this.singletonObjects) {
 *			// Consistent creation of early reference within full singleton lock
 *			singletonObject = this.singletonObjects.get(beanName);
 *			if (singletonObject == null) {
 *				singletonObject = this.earlySingletonObjects.get(beanName);
 *				if (singletonObject == null) {
 *					查看三级缓存,如果三级缓存中存在,就把依赖放入二级缓存中
 *					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
 *					if (singletonFactory != null) {
 *						singletonObject = singletonFactory.getObject();
 *						this.earlySingletonObjects.put(beanName, singletonObject);
 *						this.singletonFactories.remove(beanName);
 *					}
 * </code>
 * --->{@link DefaultSingletonBeanRegistry#addSingleton()}:将单例bean的实例对象放入Map中
 * -->{@link AbstractBeanFactory#createBean()}:创建bean实例
 * --->{@link AbstractAutowireCapableBeanFactory#createBean}:创建bean实例默认实现类
 * --->{@link AbstractAutowireCapableBeanFactory#doCreateBean}:创建bean实例
 * --->{@link AbstractAutowireCapableBeanFactory#createBeanInstance}:真正创建bean实例
 * --->{@link AbstractAutowireCapableBeanFactory#instantiateBean}:真正创建bean实例
 * ---->{@link BeanUtils#instantiateClass}:基于反射真正创建bean实例
 * --->{@link AbstractAutowireCapableBeanFactory#populateBean}:给实例化的对象属性进行赋值,并注入依赖
 * --->{@link AbstractAutowireCapableBeanFactory#initializeBean}:对原始bean对象进行增强,产生代理对象
 * --->{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization}:
 * 		调用{@link BeanPostProcessor#postProcessBeforeInitialization}进行前置处理
 * --->{@link AbstractAutowireCapableBeanFactory#invokeInitMethods}:对进行了前置处理的实例进行初始化方法调用
 * --->{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization}:
 * 		调用{@link BeanPostProcessor#postProcessAfterInitialization}进行后置处理
 * 
 * ->{@link AbstractApplicationContext#finishRefresh()}:完成刷新过程,通知生命周期处理器lifecycleProcessor刷新过程,
 * 		同时发出ContextRefreshEvent通知相关对象
 * ->{@link AbstractApplicationContext#resetCommonCaches()}:处理缓存中相同的bean
 * ->{@link AnnotationConfigServletWebServerApplicationContext#postProcessBeanFactory}
 * ->{@link ClassPathBeanDefinitionScanner#scan()}:不管是xml启动还是注解启动,都会调用该方法进行包扫描处理 ComponentScan 注解,
 * 		并将扫描后的类注入到spring容器中.用法参照{@link AnnotationConfigApplicationContext#scan(String...)}
 * ->{@link AnnotationConfigUtils#registerAnnotationConfigProcessors()}
 * -->{@link RootBeanDefinition}:以各种BeanPostProcessor实现类为构造参数,判断加载Configuration,Import,Component,ComponentScan等
 * --->{@link ConfigurationClassPostProcessor}:判断解析Configuration注解,Importor实现类,扫描{@link ComponentScan}所在包
 * ---->{@link ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry()}
 * ---->{@link ConfigurationClassPostProcessor#processConfigBeanDefinitions()}
 * ---->{@link ConfigurationClassUtils#checkConfigurationClassCandidate()}:判断是否为Configuration,设置相关属性
 * ---->{@link ConfigurationClassUtils#isConfigurationCandidate()}:判断是否为Import,Component,ComponentScan,ImportResource
 * --->{@link AutowiredAnnotationBeanPostProcessor}:判断解析Autowired,Value,javax.inject.Inject注解
 * --->{@link CommonAnnotationBeanPostProcessor}:判断解析javax.xml.ws.WebServiceRef,javax.ejb.EJB注解
 * --->{@link EventListenerMethodProcessor}:判断解析EventListenerFactory的实现类
 * ->{@link AnnotationConfigUtils#registerPostProcessor()}
 * ->{@link BeanDefinitionRegistry#registerBeanDefinition()}
 * </pre>
 * 
 * SpringBoot启动流程--{@link AbstractApplicationContext#obtainFreshBeanFactory}
 * 
 * 1.{@link AbstractApplicationContext#obtainFreshBeanFactory()}:告诉子类去刷新beanFactory,返回{@link DefaultListableBeanFactory}
 * 2.{@link AbstractRefreshableApplicationContext#refreshBeanFactory}:刷新创建beanFactory,解析XML,注解,注册
 * BeanDefinition
 * 3.{@link AbstractRefreshableApplicationContext#hasBeanFactory}:判断当前上下文是否已经存在beanFactory,
 * 比如刷新了几次和未关闭的beanFactory.如果有就销毁所有的在这个上下文管理的beans,同时关闭beanFactory
 * 4.{@link AbstractRefreshableApplicationContext#createBeanFactory}:创建
 * {@link BeanFactory}
 * 5.{@link AbstractRefreshableApplicationContext#customizeBeanFactory}:对新创建的beanFactory定制化
 * 6.{@link AbstractRefreshableApplicationContext#loadBeanDefinitions}:加载bean的定义
 * ->6.1.{@link AbstractXmlApplicationContext#loadBeanDefinitions}:主要加载XML资源,包括从Resource资源对象和资源路径加载
 * ->6.1.1.{@link AbstractBeanDefinitionReader#loadBeanDefinitions}:读取XML配置文件,加载
 * BeanDefinition
 * ->6.1.1.1.{@link XmlBeanDefinitionReader#loadBeanDefinitions(EncodedResource)}:解析XML,加载
 * BeanDefinition
 * ->6.1.1.2.{@link XmlBeanDefinitionReader#doLoadBeanDefinitions()}:解析XML文件,加载箣竹
 * BeanDefinition
 * ->6.1.1.2.1.{@link DefaultBeanDefinitionDocumentReader#processBeanDefinition()}:处理XML生成的bean定义
 * ->6.1.1.2.2.{@link BeanDefinitionReaderUtils#registerBeanDefinition()}:注册最后的BeanDefinitionHolder对象
 * ->6.1.1.2.3.{@link DefaultListableBeanFactory#registerBeanDefinition()}:将bean定义放入到beanFactory的Map缓存中
 * 7.{@link AbstractRefreshableApplicationContext#getBeanFactory}:返回刚才GenericApplicationContext创建的BeanFactory对象
 * 
 * SpringBoot启动流程--{@link AbstractApplicationContext#invokeBeanFactoryPostProcessors}
 * 
 * <pre>
 * 1.{@link AbstractApplicationContext#invokeBeanFactoryPostProcessors}:处理bean容器,加载自动配置,注册bean等
 * 2.{@link #PostProcessorRegistrationDelegate#invokeBeanDefinitionRegistryPostProcessors}:循环注册beanDefinition
 * 3.{@link ConfigurationClassPostProcessor#processConfigBeanDefinitions()}:beanDefinition注册
 * 4.{@link #ConfigurationClassParser#parse()}:解析扫描启动类以及自动配置类
 * 5.{@link #ConfigurationClassParser#doProcessConfigurationClass()}:解析启动类以及自动配置类,加载@Bean,@Import等相关注解
 * 6.{@link #ConfigurationClassParser#processImports()}:解析自动配置类以及{@link ImportSelector},{@link DeferredImportSelector}
 * 7.{@link #ConfigurationClassParser$DeferredImportSelectorHandler#handle()}:解析AutoConfigurationImportSelector,
 * 		该类由{@link EnableAutoConfiguration}引入,加载所有自动配置类
 * 8.{@link TransactionAutoConfiguration}:所有自动配置在此处被处理,包括AOP相关类AnnotationAwareAspectJAutoProxyCreator
 * </pre>
 * 
 * SpringBoot启动流程--{@link AbstractApplicationContext#initApplicationEventMulticaster}
 * 
 * <pre>
 * 1.{@link AbstractApplicationContext#initApplicationEventMulticaster()}:应用广播消息,发布监听消息
 * 2.{@link AbstractApplicationContext#onRefresh()}:初始化其他的bean,默认情况下Spring什么也不做
 * ->2.1.{@link ServletWebServerApplicationContext#onRefresh}:Web项目中是调用该实现类
 * ->2.1.1.{@link ServletWebServerApplicationContext#createWebServer}:创建Web容器
 * ->2.1.1.1.{@link TomcatServletWebServerFactory#getWebServer}:默认由Tomcat创建Web容器
 * ->2.1.1.1.1{@link SpringServletContainerInitializer}:该类不进行任何实质化的操作,具体的的操作应该交给
 * 			{@link WebApplicationInitializer}的具体实现类完成,比如说 {@link DispatcherServlet},listeners等
 * </pre>
 * 
 * SpringBoot启动流程--{@link AbstractApplicationContext#finishBeanFactoryInitialization}
 * 
 * <pre>
 * 1.{@link AbstractApplicationContext#finishBeanFactoryInitialization}:处理剩余的非Lazy的单例bean
 * 2.{@link DefaultListableBeanFactory#preInstantiateSingletons}:处理剩余的非Lazy的单例bean
 * 3.{@link SmartInitializingSingleton#afterSingletonsInstantiated}: 处理剩余的单例bean,此处有多种处理程序,包括监听(listener)等
 * ->3.1.{@link EventListenerMethodProcessor#afterSingletonsInstantiated()}:负责监听器的单例处理
 * ->3.1.1.{@link EventListenerMethodProcessor#processBean()}:只处理带有@EventListener注解的bean
 * ->3.1.2.{@link MethodIntrospector#selectMethods}:获得带有@EventListener注解的方法
 * </pre>
 * 
 * {@link ApplicationContextInitializer}:在spring调用refreshed方法之前调用该方法.是为了对spring容器做进一步的控制
 * 注入实现了该类的方法有2种:Configuration或者在META-INF的spring.factories中添加该类,可参照spring-autoconfigure包里的添加
 * {@link CommandLineRunner}:在容器启动成功之后的最后一个回调,该回调执行之后容器就成功启动
 * {@link ApplicationEvent}:自定义事件,需要发布的事件继承该接口
 * {@link ApplicationListener}:事件监听.可以直接在listener上添加注解或者使用上下文添加到容器中
 * {@link publishEvent}:发布事件,必须在refreshed之后调用.使用任何继承了上下文的context调用,传入ApplicationEvent
 * 
 * Bean的加载解析实例化:
 * 
 * <pre>
 * {@link DefaultListableBeanFactory}:类中的beanDefinitionMap属性维护着封装好的BeanDefinition定义
 * ->{@link DefaultSingletonBeanRegistry}:DefaultListableBeanFactory的父类,singletonObjects属性维护着单例Bean实例缓存,
 * 		由beanDefinitionMap经过一系列操作后转化的单例对象
 * {@link GenericBeanDefinition}: 通用bean实现,新加入的bean文件配置属性定义类,是ChildBeanDefinition和RootBeanDefinition更好的替代者.
 * 		Spring初始化时,会用GenericBeanDefinition或是ConfigurationClassBeanDefinition (用@Bean注解注释的类)存储用户自定义的Bean,
 * 		在初始化Bean时,又会将其转换为RootBeanDefinition.
 * 		GenericBeanDefinition的patentName属性指定了当前类的父类,最重要的是它实现了parentName属性的setter、getter函数,
 * 		RootBeanDefinition没有parentName属性,对应的getter函数只是返回null,setter函数不提供赋值操作,
 * 		也就是说RootBeanDefinition不提供继承相关的操作,但是初始化时使用的是RootBeanDefinition,那父类的性质如何体现?
 * 		这里子类会覆盖父类中相同的属性,所以Spring会首先初始化父类的RootBeanDefinition,
 * 		然后根据子类的GenericBeanDefinition覆盖父类中相应的属性,最终获得子类的RootBeanDefinition
 * {@link AnnotatedGenericBeanDefinition}: 存储@Configuration注解注释的类
 * {@link ScannedGenericBeanDefinition}: 存储@Component,@Service,@Controller等注解注释的类
 * {@link BeanDefinitionReader}:Bean读取,主要从XML或注解中读取必须的信息,由实现类读取
 * ->{@link XmlBeanDefinitionReader},{@link AnnotatedBeanDefinitionReader},{@link PropertiesBeanDefinitionReader}
 * </pre>
 * 
 * 基于XML的bean实例化流程:
 * 
 * <pre>
 * 加载xml配置文件,解析获取配置中的每个的信息,封装成一个个的BeanDefinition对象
 * 将BeanDefinition存储在 DefaultListableBeanFactory 类的beanDefinitionMap属性中
 * ApplicationContext底层遍历beanDefinitionMap,创建Bean实例对象
 * 创建好的Bean实例对象,被存储到DefaultSingletonBeanRegistry类的singletonObjects属性
 * ->DefaultSingletonBeanRegistry为DefaultListableBeanFactory的父类
 * ->singletonObjects是由BeanFactory将beanDefinitionMap转换而来
 * 当执行applicationContext.getBean(beanName)时,从singletonObjects去匹配Bean实例返回
 * </pre>
 * 
 * Bean实例化的一些特殊接口
 * 
 * <pre>
 * {@link FactoryBean}:工厂bean,类似于抽象工厂模式中返回实例接口的工厂,主要返回同一类的bean实例
 * {@link BeanFactory}:Spring容器,默认情况是{@link DefaultListableBeanFactory},加载了Spring中组件及相关参数
 * {@link BeanFactoryPostProcessor}:在Spring容器初始化之后调用,可对容器做修改,Spring本身没有做扩展
 * {@link BeanDefinitionRegistryPostProcessor}:对{@link BeanDefinition}进行修改,不对实例进行修
 * {@link BeanPostProcessor}:在接口或类初始化之前,之后进行的操作,对实例进行修改.AOP主要接口实现该接口
 *	{@link BeanPostProcessor#postProcessBeforeInitialization()}:bean初始化,属性设置完之后调用,比如afterPropertiesSet,init等
 * 	{@link BeanPostProcessor#postProcessAfterInitialization()}:bean初始化,属性设置,afterPropertiesSet,init等完成后调用,比如AOP,事务等
 * ->{@link InstantiationAwareBeanPostProcessor}:BeanPostProcessor 子接口,该接口在实例化之前添加回调,
 * 		并在实例化之后但在set或 Autowired 注入之前添加回调
 * -->{@link AutowiredAnnotationBeanPostProcessor}:BeanPostProcessor 实现类,加载由{@link Autowired}和{@link Value}
 * 		修饰的变量,构造等,支持{@link Inject},由{@link BeanUtils#instantiateClass}实例化,
 * 		{@link AutowiredAnnotationBeanPostProcessor#postProcessMergedBeanDefinition()}主要是该方法完成注入
 * -->{@link CommonAnnotationBeanPostProcessor}:作用同AutowiredAnnotationBeanPostProcessor,加载{@link Resource}以及jakarta.ejb.EJB
 * -->{@link AnnotationConfigApplicationContext},{@link AnnotationConfigWebApplicationContext}:
 * 		根据环境不同启动加载{@link Configuration}
 * -->{@link AbstractAutoProxyCreator}:BeanPostProcessor 实现,用AOP代理包装每个符合条件的bean,
 * 		在调用bean本身之前委托给指定的拦截器
 * ->{@link AutowireCapableBeanFactory}:提供工厂的装配功能
 * ->{@link HierarchicalBeanFactory}:提供父容器的访问功能
 * -->{@link ConfigurableBeanFactory}:提供factory的配置功能,API等
 * --->{@link AbstractBeanFactory}:实现了 ConfigurableBeanFactory 大部分功能
 * ---->{@link AbstractAutowireCapableBeanFactory}:同时实现了 AbstractBeanFactory 和 AutowireCapableBeanFactory
 * ----->{@link DefaultListableBeanFactory}:BeanFactory的默认实现类,包含IOC容器具备的重要功能,是一个完整的容器实现类
 * --->{@link ConfigurableListableBeanFactory}:集大成者,提供解析,修改bean定义,并初始化单例
 * ->{@link ListableBeanFactory}:提供容器内bean实例的枚举功能,但不会考虑父容器内的实例
 * {@link Value}:将配置文件中的值或系统值赋值给某个变量.该注解由{@link BeanPostProcessor}的实现类实现,
 * 		所以不能在 BeanPostProcessor,BeanFactoryPostProcessor 的实现类中使用,会造成循环引用,可使用{@link Autowired}代替
 * {@link InitializingBean}:初始化bean,在bean加载完之后,初始化之前执行,在 {@link PostConstruct}初始化之前执行
 * {@link ApplicationContextInitializer}:在Spring容器调用refresh()之前的回调,此时bean还未初始化,只能对容器做操作
 * </pre>
 * 
 * {@link SpringServletContainerInitializer}:该类负责对容器启动时相关组件进行初始化,当前类只是完成一些验证和组件装配,
 * 具体初始化由类上注解{@link HandlesTypes}的值决定,实际上就是{@link WebApplicationInitializer}的实现类.
 * #TomcatStarter:该类并非通过SPI机制实例化,因为该类非public,也无无参构造,本质上是通过new实例化的.
 * 该类和{@link SpringServletContainerInitializer}类似,主要初始化是通过{@link ServletContextInitializer}完成
 * 
 * Jar启动原理:
 * 
 * <pre>
 * 通过打包后目录下的META-INF/MANIFEST.MF文件指定用户定义的启动类,指定加载lib和classes的类
 * Start-Class:指定JAR包的启动类,即用户自定义的启动类,添加了{@link SpringBootApplication}的类
 * Main-Class: 指定{@link JarLauncher}加载用户自定义类以及扩展引用JAR包
 * Spring-Boot-Classes:指定用户class目录,默认BOOT-INF/classes/
 * Spring-Boot-Lib:指定引用JAR包目录,默认BOOT-INF/lib/
 * </pre>
 * 
 * 可以参考用来写自定义注入,扫包,扫注解等方法事例
 * 
 * <pre>
 * {@link ClassPathScanningCandidateComponentProvider}:可以对指定包进行扫描并注入,见{@link EurekaServerAutoConfiguration#jerseyApplication}
 * {@link AnnotationTypeFilter}:扫描时指定扫描拦截器,见{@link FeignClientsRegistrar#registerFeignClients}
 * </pre>
 * 
 * Spring上下文初始化,可扩展点
 * 
 * <pre>
 * {@link ApplicationContextInitializer#initialize(ConfigurableApplicationContext)}:
 * 		eg:{@link SelfApplicationContextInitializer}
 * {@link AbstractApplicationContext#refresh()}:
 * 		刷新上下文,加载Bean定义,注解,等
 * {@link BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry()}:
 * 		eg:{@link SelfBeanDefinitionRegistryPostProcessor}
 * {@link BeanDefinitionRegistryPostProcessor#postProcessBeanFactory(ConfigurableListableBeanFactory)}:
 * 		eg:{@link SelfBeanDefinitionRegistryPostProcessor}
 * {@link BeanFactoryPostProcessor#postProcessBeanFactory(ConfigurableListableBeanFactory)}:
 * 		eg:{@link SelfBeanFactoryPostProcessor}
 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInitialization(Object, String)}:
 * 		eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link SmartInstantiationAwareBeanPostProcessor#determineCandidateConstructors(Class, String)}:
 * 		eg:{@link SelfSmartInstantiationAwareBeanPostProcessor}
 * {@link MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition()}
 * {@link InstantiationAwareBeanPostProcessor#postProcessAfterInitialization(Object, String)}:
 * 		eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference(Object, String)}:
 * 		eg:{@link SelfSmartInstantiationAwareBeanPostProcessor}
 * {@link BeanFactoryAware#setBeanFactory(BeanFactory)}:
 * 		eg:{@link SelfBeanFactoryAware}
 * {@link InstantiationAwareBeanPostProcessor#postProcessPropertyValues()}:
 * 		eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link ApplicationContextAwareProcessor#invokeAwareInterfaces()}:
 * 		eg:{@link SelfApplicationContextAwareProcessor}
 * {@link BeanNameAware#setBeanName(String)}:
 * 		eg:{@link SelfBeanNameAware}
 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInitialization(Object, String)}:
 * 		eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link PostConstruct}:
 * 		在bean初始化阶段,先调用{@link BeanPostProcessor#postProcessBeforeInitialization()}之后,
 * 		再调用被 PostConstruct 注解修饰的方法,之后再调用{@link InitializingBean#afterPropertiesSet()}
 * {@link InitializingBean#afterPropertiesSet()}:
 * 		eg:{@link SelfInitializingBean}
 * {@link InstantiationAwareBeanPostProcessor#postProcessAfterInitialization(Object, String)}:
 * 		eg:{@link SelfInstantiationAwareBeanPostProcessor}
 * {@link FactoryBean#getObject()}:
 * 		eg:{@link SelfFactoryBean}
 * {@link SmartInitializingSingleton#afterSingletonsInstantiated()}:
 * 		eg:{@link SelfSmartInitializingSingleton}
 * {@link CommandLineRunner#run(String...)}:
 * 		eg:{@link SelfCommandLineRunner}
 * {@link DisposableBean#destroy()}:
 * 		eg:{@link SelfDisposableBean}
 * </pre>
 * 
 * {@link BeanFactory}和 {@link ApplicationContext}的不同:
 * 
 * <pre>
 * 1.BeanFactory是Bean工厂,ApplicationContext是Spring容器
 * 2.ApplicationContext是对BeanFactory的扩展,增加了监听,国际化等功能
 * 3.ApplicationContext继承了BeanFactory,维持了BeanFactory的引用
 * 4.BeanFactory在首次初始化Bean时才创建Bean,但ApplicationContext在配置文件加载完,容器一创建就将Bean实例化并初始化好
 * </pre>
 * 
 * 一些特殊类:
 * 
 * <pre>
 * {@link ServletWebServerFactoryAutoConfiguration}:Web容器自动配置类
 * {@link AutoConfigurationPackages}:获得自动配置时获得的基础扫描包路径以及类等信息
 * {@link AnnotationConfigApplicationContext}:根据被注解修改类或包扫描进行上下文生成的容器,手动启动应用时可用,
 * 		功能和使用{@link SpringBootApplication}启动时的{@link AnnotationConfigServletWebServerApplicationContext}类似
 * {@link AnnotationConfigApplicationContext#register()}:如果是通过类启动,则扫描该类上的注解进行启动,并将扫描到的类注入到容器中
 * {@link AnnotationConfigApplicationContext#scan()}:扫描指定包进行bean的注入
 * {@link AnnotationConfigUtils#registerAnnotationConfigProcessors()}:将指定的bean注入到spring容器中
 * {@link AnnotatedBeanDefinitionReader#doRegisterBean()}:被指定注解修饰的类读取类
 * {@link ClassPathScanningCandidateComponentProvider#registerDefaultFilters()}:根据默认的拦截器,扫描{@link Component}
 * </pre>
 * 
 * 一些Aware,大部分都是再bean实例话之后,初始化之前调用:
 * 
 * <pre>
 * {@link BeanFactoryAware}: 详见{@link SelfBeanFactoryAware}
 * {@link ServletContextAware}: spring回调方法注入ServletContext,Web环境才生效
 * {@link BeanNameAware}: 详见{@link SelfBeanNameAware}
 * {@link EnvironmentAware}: 详见{@link SelfApplicationContextAwareProcessor}
 * {@link EmbeddedValueResolverAware}: 详见{@link SelfApplicationContextAwareProcessor}
 * {@link ResourceLoaderAware}: 详见{@link SelfApplicationContextAwareProcessor}
 * {@link ApplicationEventPublisherAware}: 详见{@link SelfApplicationContextAwareProcessor}
 * {@link MessageSourceAware}: 详见{@link SelfApplicationContextAwareProcessor}
 * {@link ApplicationContextAware}: 详见{@link SelfApplicationContextAwareProcessor}
 * </pre>
 * 
 * Spring根目录下META-INF下各种文件作用
 * 
 * <pre>
 * spring.factories:各种自动配置,监听器,初始化器等类的全路径名,由{@link EnableAutoConfiguration}注解加载
 * spring.handlers:如果需要自定义xml的命名空间,需要写在该文件中,需要实现接口{@link NamespaceHandler},自定义处理xml
 * spring.schemas:各种xml的xsd不同版本的文件格式约束映射关系以及xsd文件在包中的位置
 * spring-configuration-metadata.json:{@link ConfigurationProperties}修饰的类编译后产生的自定义属性提示文件
 * </pre>
 * 
 * Spring XML方式整合第三方框架
 * 
 * <pre>
 * 1.确定命名空间名称(自定义),schema虚拟路径(需要在包中能找到,参照spring-context下的spring.schemas写法),标签名称(自定义)
 * 2.编写schema约束文件dream-annotation.xsd,参照org/springframework/context/config/spring-context.xsd
 * 3.在类加载路径下创建META-INF目录,修改spring.schemas和处理器映射文件spring.handlers
 * 4.编写命名空间处理器 DreamNamespaceHandler,在init方法中注册DreamBeanDefinitionParser
 * 5.编写标签的解析器HaohaoBeanDefinitionParser,在parse方法中注册HaohaoeanPostProcessor
 * 6.编写DreamBeanPostProcessor后置处理器
 * 7.使用时在applicationContext.xml配置文件中引入命名空间,在applicationContext.xml配置文件中使用自定义的标签
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-12-02 15:16:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableAsync
public class Application {

	public static void main(String[] args) {
		/**
		 * 第一种启动方式,直接run即可启动,返回的上下文可以做一些其他操作
		 * 
		 * @param args 该参数由启动时传递而来
		 */
		SpringApplication.run(Application.class, args);
		/**
		 * 第二种启动方式,启动时指定一些特定参数
		 */
		// SpringApplication application = new SpringApplication(Application.class);
		// application.setBannerMode(Banner.Mode.OFF);
		// 代码方式指定启动的环境,等同于spring.profiles.active,优先级未定
		// application.setAdditionalProfiles("config,dev,mail");
		// application.run(args);

		/**
		 * 第三种启动方式,链式调用
		 */
		// new
		// SpringApplicationBuilder(Application.class).bannerMode(Banner.Mode.OFF).build(args);
	}
}