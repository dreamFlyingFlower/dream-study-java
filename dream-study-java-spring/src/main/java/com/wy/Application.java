package com.wy;

import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

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
 * {@link Profile}:指定某个类,某个方法在指定环境下才有效
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
 * SpringBoot启动流程:
 * 
 * <pre>
 * 1.{@link SpringApplication#run(Class, String...)}:构造方法判断是否是SERVLET,REACTIVE,NONE环境
 * 2.{@link SpringApplication#setInitializers}:加载所有META-INF/spring.factories中的{@link ApplicationContextInitializer}
 * 3.{@link SpringApplication#setListeners}:加载所有META-INF/spring.factories中的{@link ApplicationListener}
 * 4.{@link SpringApplication#deduceMainApplicationClass}:推断main所在的类
 * 5.{@link SpringApplication#run(String...)}:开始执行run()
 * 6.{@link SpringApplication#configureHeadlessProperty}:设置java.awt.headless系统变量
 * 7.{@link SpringApplication#getRunListeners}:加载所有META-INF/spring.factories中的{@link SpringApplicationRunListener}
 * 8.执行所有的{@link SpringApplicationRunListener#starting()}
 * 9.{@link DefaultApplicationArguments}:实例化ApplicationArguments对象
 * 10.{@link SpringApplication#prepareEnvironment}:创建Environment
 * 11.{@link SpringApplication#configureEnvironment}:配置Enviroment,主要是把run()的参数配置到Environment中
 * 12.执行所有{@link SpringApplicationRunListener#environmentPrepared()}
 * 13.{@link SpringApplication#configureIgnoreBeanInfo}:设置需要忽略的环境变量
 * 14.{@link SpringApplication#printBanner}:设置日志打印Banner
 * 15.{@link SpringApplication#createApplicationContext}:根据环境不同,设置不同的ConfigurableApplicationContext
 * 16.{@link SpringApplication#getSpringFactoriesInstances}:加载自定义的异常报告
 * 17.{@link SpringApplication#prepareContext}:预加载ConfigurableApplicationContext
 * 18.{@link SpringApplication#postProcessApplicationContext}:加载beanNameGenerator,resourceLoade,raddConversionService
 * 19.{@link SpringApplication#applyInitializers}:回调所有的{@link ApplicationContextInitializer#initialize}
 * 20.执行所有的{@link SpringApplicationRunListener#contextPrepared()}
 * 21.{@link SpringApplication#load}:设置beanNameGenerator,resourceLoade,raddConversionService
 * 22.执行所有的{@link SpringApplicationRunListener#contextLoaded()}
 * 23.{@link SpringApplication#refreshContext}:调用context的registerShutdownHook(),执行context的refresh(),
 * 		根据不同环境执行不同的refresh(),最终调用{@link AbstractApplicationContext#refresh()},注册各种Bean以及Spring组件
 * 24.{@link SpringApplication#afterRefresh}:用户可自定义加载完成的方法
 * 25.执行所有的{@link SpringApplicationRunListener#started()}
 * 26.{@link SpringApplication#callRunners}:回调,获取容器中所有的{@link ApplicationRunner},{@link CommandLineRunner},依次调用
 * 27.执行所有的{@link SpringApplicationRunListener#running()}
 * 28.返回{@link ConfigurableApplicationContext}
 * </pre>
 * 
 * SpringBoot启动具体流程:
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
 * 
 * {@link SpringApplication#refreshContext()}:通过XML,注解构建SpringBean,AOP等实例的主要方法
 * ->{@link AbstractApplicationContext#refresh()}:同步刷新上下文,初始化SpringBean,处理各种 BeanPostProcessor,AOP
 * ->{@link AbstractApplicationContext#prepareRefresh()}:预刷新,初始化配置文件,读取{@link Environment}相关参数
 * ->{@link AbstractApplicationContext#obtainFreshBeanFactory()}:告诉子类去刷新内部的beanFactory,获得刷新后的beanFactory,
 * 		最终返回{@link DefaultListableBeanFactory},默认的beanFactory.主要就是根据XML创建 BeanDefinition
 * -->{@link AbstractRefreshableApplicationContext#refreshBeanFactory}:刷新beanFactory,解析XML,注解,注册 BeanDefinition
 * -->{@link AbstractRefreshableApplicationContext#hasBeanFactory}:判断当前上下文是否已经存在beanFactory,
 * 		比如刷新了几次和未关闭的beanFactory.如果有就销毁所有的在这个上下文管理的beans,同时关闭beanFactory
 * -->{@link AbstractRefreshableApplicationContext#createBeanFactory}:创建 {@link BeanFactory}
 * -->{@link AbstractRefreshableApplicationContext#customizeBeanFactory}:对新创建的beanFactory定制化
 * -->{@link AbstractRefreshableApplicationContext#loadBeanDefinitions}:加载bean的定义
 * --->{@link AbstractXmlApplicationContext#loadBeanDefinitions}:加载bean的定义
 * ---->{@link AbstractBeanDefinitionReader#loadBeanDefinitions}:读取XML配置文件,加载bean定义
 * ----->{@link XmlBeanDefinitionReader#loadBeanDefinitions(EncodedResource)}:加载bean定义
 * ----->{@link XmlBeanDefinitionReader#doLoadBeanDefinitions()}:解析XML文件,出则bean定义
 * ------>{@link DefaultBeanDefinitionDocumentReader#processBeanDefinition()}:处理XML生成的bean定义
 * ------->{@link BeanDefinitionReaderUtils#registerBeanDefinition()}:注册最后的BeanDefinitionHolder对象
 * -------->{@link DefaultListableBeanFactory#registerBeanDefinition()}:将bean定义放入到beanFactory的Map缓存中
 * 
 * ->{@link AbstractApplicationContext#prepareBeanFactory()}:预处理beanFactory,为在上下文中使用,注册默认environment,
 * 		systemEnvironment,systemProperties
 * ->{@link AbstractApplicationContext#postProcessBeanFactory()}:由子类实现该方法,Spring不做任何处理
 * ->{@link AbstractApplicationContext#invokeBeanFactoryPostProcessors()}:获得包扫描时由注解标注的bean class,然后放入上下文.
 * 		激活各种BeanFactory处理器,目前BeanFactory没有注册任何BeanFactoryPostProcessor,此处相当于不做任何处理.
 * 		MyBatis就是在此处注入了#MapperScannerConfigurer,从而进一步解析MyBatis XML
 * ->{@link AbstractApplicationContext#registerBeanPostProcessors()}:注册拦截Bean创建的Bean处理器,
 * 		如果没有BeanProcessors,不做任何处理
 * ->{@link AbstractApplicationContext#initMessageSource()}:在上下文初始化注册MessageaSource的bean,国际化语言处理
 * ->{@link AbstractApplicationContext#initApplicationEventMulticaster()}:在上下文初始化注册applicationEventMulticaster的bean,
 * 		应用广播消息
 * ->{@link AbstractApplicationContext#onRefresh()}:初始化其他的bean,默认情况下Spring什么也不做
 * -->{@link ServletWebServerApplicationContext#onRefresh}:Web项目中是调用该实现类
 * -->{@link ServletWebServerApplicationContext#createWebServer}:创建Web容器
 * --->{@link TomcatServletWebServerFactory#getWebServer}:默认由Tomcat创建Web容器
 * ---->{@link SpringServletContainerInitializer}:该类不进行任何实质化的操作,具体的的操作应该交给
 * 			{@link WebApplicationInitializer}的具体实现类完成,比如说 {@link DispatcherServlet},listeners等
 * ->{@link AbstractApplicationContext#registerListeners()}:在所有bena中查找listener bean并注册到消息广播中
 * 
 * ->{@link AbstractApplicationContext#finishBeanFactoryInitialization()}:初始化所有剩下的非延迟初始化的单例bean对象实例
 * -->{@link AbstractBeanFactory#doGetBean()}:执行获得bean实例的方法
 * -->{@link AbstractBeanFactory#getSingleton()}:获得bean实例的单例对象
 * --->{@link DefaultSingletonBeanRegistry#getSingleton()}:获得bean实例的单例对象的实际操作类
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
 * ->{@link ClassPathBeanDefinitionScanner.scan()}
 * ->{@link AnnotationConfigUtils.registerAnnotationConfigProcessors()}
 * -->{@link RootBeanDefinition(ConfigurationClassPostProcessor.class)}:判断加载Configuraion,Import,Component,ComponentScan等
 * --->{@link ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry()}
 * --->{@link ConfigurationClassPostProcessor.processConfigBeanDefinitions()}
 * --->{@link ConfigurationClassUtils.checkConfigurationClassCandidate()}:判断是否为Configuration,设置相关属性
 * --->{@link ConfigurationClassUtils.isConfigurationCandidate()}:判断是否为Import,Component,ComponentScan,ImportResource
 * ->{@link AnnotationConfigUtils.registerPostProcessor()}
 * ->{@link BeanDefinitionRegistry.registerBeanDefinition()}
 * </pre>
 *
 * {@link AutowiredAnnotationBeanPostProcessor}:加载由 Autowired
 * 和{@link Value}修饰的变量,支持{@link Inject}, 由{@link BeanUtils#instantiateClass}实例化
 * {@link AnnotationConfigApplicationContext},{@link AnnotationConfigWebApplicationContext}:
 * 根据环境不同启动加载{@link Configuration}
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
 * {@link BeanDefinitionReader}:Bean读取,主要从XML或注解中读取必须的信息,由实现类读取
 * ->{@link XmlBeanDefinitionReader},{@link AnnotatedBeanDefinitionReader},{@link PropertiesBeanDefinitionReader}
 * </pre>
 * 
 * Bean实例化的一些特殊接口
 * 
 * <pre>
 * {@link FactoryBean}:工厂bean,类似于抽象工厂模式中返回实例接口的工厂
 * {@link BeanFactory}:Spring容器,默认情况是{@link DefaultListableBeanFactory},加载了Spring中组件及相关参数
 * {@link BeanPostProcessor}:在接口或类初始化之前,之后进行的操作.AOP主要接口实现该接口
 * ->{@link BeanPostProcessor#postProcessBeforeInitialization()}:初始化前进行的后置操作,比如afterPropertiesSet,init等
 * ->{@link BeanPostProcessor#postProcessAfterInitialization()}:初始化后进行的后置操作,比如AOP,事务等
 * {@link InstantiationAwareBeanPostProcessor}:BeanPostProcessor 子接口,该接口在实例化之前添加回调,
 * 		并在实例化之后但在set或 Autowired 注入之前添加回调
 * {@link AbstractAutoProxyCreator}:BeanPostProcessor 实现,用AOP代理包装每个符合条件的bean,
 * 		在调用bean本身之前委托给指定的拦截器
 * {@link BeanFactoryPostProcessor}:对{@link BeanDefinition}进行修改
 * {@link Value}:将配置文件中的值或系统值赋值给某个变量.
 * 		该注解由{@link BeanPostProcessor#postProcessBeforeInitialization()}的实现类实现,
 * 		所以不能在 BeanPostProcessor,BeanFactoryPostProcessor 的实现类中使用,会造成循环引用,可使用{@link Autowired}代替
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-12-02 15:16:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
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