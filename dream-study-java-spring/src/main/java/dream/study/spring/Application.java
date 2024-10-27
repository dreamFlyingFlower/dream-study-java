package dream.study.spring;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.annotation.HandlesTypes;

import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
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
import org.springframework.beans.factory.config.SingletonBeanRegistry;
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
import org.springframework.context.LifecycleProcessor;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import dream.study.spring.initializer.SelfServletContainerInitializer;
import dream.study.spring.listener.SelfApplicationListener;
import dream.study.spring.scalable.example.SelfApplicationContextAwareProcessor;
import dream.study.spring.scalable.example.SelfBeanFactoryAware;
import dream.study.spring.scalable.example.SelfBeanNameAware;

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
 * 		{@link DestructionAwareBeanPostProcessor},{@link InstantiationAwareBeanPostProcessor},
 * 		{@link SmartInstantiationAwareBeanPostProcessor},{@link MergedBeanDefinitionPostProcessor}:[internalPostProcessors].
 * 		注册流程同BeanFactoryPostProcessor,只在最后多了注册MergedBeanDefinitionPostProcessor.
 * 		注册一个ApplicationListenerDetector来在Bean创建完成后检查是否是ApplicationListener,如果是则在初始化完成后添加到容器中.
 * 
 * ->{@link AbstractApplicationContext#initMessageSource()}:注册MessageaSource,国际化语言处理,消息绑定,消息解析.
 *		获取BeanFactory,查看容器中是否有id为messageSource,类型是MessageSource的组件:
 *		如果有赋值给messageSource,如果没有自己创建一个DelegatingMessageSource.之后将创建好的MessageSource注册在容器中
 * 
 * ->{@link AbstractApplicationContext#initApplicationEventMulticaster()}:注册applicationEventMulticaster,应用广播消息,发布监听
 * 		获取BeanFactory,从BeanFactory中获取applicationEventMulticaster的ApplicationEventMulticaster;
 * 		如果没有该bean,创建一个{@link SimpleApplicationEventMulticaster},将创建的ApplicationEventMulticaster添加到BeanFactorv中
 * 
 * ->{@link AbstractApplicationContext#onRefresh()}:子容器实现,可以在容器刷新时自定义逻辑
 * 
 * ->{@link AbstractApplicationContext#registerListeners()}:在所有bena中查找listener bean并注册到消息广播中
 *		从容器中获得所有的ApplicationListener,将每个监听器添加到事件派发器ApplicationEventMulticaster中,再派发之前步骤产生的事件
 * 
 * ->{@link AbstractApplicationContext#finishBeanFactoryInitialization()}:初始化所有剩下的非延迟初始化的单例bean对象实例,
 * 		Bean的IOC,DI,AOP都是在该方法中调用
 * 
 * --->{@link DefaultSingletonBeanRegistry#addSingleton()}:将单例bean的实例对象放入Map中
 * -->{@link AbstractBeanFactory#createBean()}:创建bean实例
 * --->{@link AbstractAutowireCapableBeanFactory#createBean}:创建bean实例默认实现类
 * --->{@link AbstractAutowireCapableBeanFactory#doCreateBean}:创建bean实例
 * --->{@link AbstractAutowireCapableBeanFactory#createBeanInstance}:真正创建bean实例
 * --->{@link AbstractAutowireCapableBeanFactory#instantiateBean}:真正创建bean实例
 * ---->{@link BeanUtils#instantiateClass}:基于反射真正创建bean实例
 * --->{@link AbstractAutowireCapableBeanFactory#populateBean}:给实例化的对象属性进行赋值,并注入依赖,可能产生循环依赖
 * ---->{@link AbstractAutowireCapableBeanFactory#applyPropertyValues}:设置属性值
 * ----->{@link #BeanDefinitionValueResolver#resolveValueIfNecessary}:判断是否需要解析值
 * ----->{@link #BeanDefinitionValueResolver#resolveReference}:解析填充值,会再次调用{@link AbstractBeanFactory#doGetBean}
 * ----->{@link AbstractBeanFactory#getSingleton}:从一级缓存获得bean,同时解决循环依赖
 * ------>{@link DefaultSingletonBeanRegistry#getSingleton(String, Boolean)}:利用三级缓存解决循环依赖,但是只能解决set方式
 * ------>三级缓存解决循环依赖:singletonObjects:一级缓存;earlySingletonObjects:二级缓存;singletonFactories:三级缓存
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
 *					查看三级缓存,如果三级缓存中存在,就把未成型的依赖放入二级缓存中
 *					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
 *					if (singletonFactory != null) {
 *						singletonObject = singletonFactory.getObject();
 *						this.earlySingletonObjects.put(beanName, singletonObject);
 *						this.singletonFactories.remove(beanName);
 *					}
 * </code>
 * 
 * --->{@link AbstractAutowireCapableBeanFactory#initializeBean}:对原始bean对象进行增强,产生代理对象
 * --->{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization}:
 * 		调用{@link BeanPostProcessor#postProcessBeforeInitialization}进行前置处理
 * --->{@link AbstractAutowireCapableBeanFactory#invokeInitMethods}:对进行了前置处理的实例进行初始化方法调用
 * --->{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization}:
 * 		调用{@link BeanPostProcessor#postProcessAfterInitialization}进行后置处理
 * 
 * ->{@link AbstractApplicationContext#finishRefresh()}:完成刷新,通知生命周期处理器lifecycleProcessor,发布ContextRefreshEvent事件
 * 
 * ->{@link AbstractApplicationContext#resetCommonCaches()}:处理缓存中相同的bean
 * </pre>
 * 
 * SpringBoot启动流程--{@link AbstractApplicationContext#postProcessBeanFactory}
 * 
 * <pre>
 * 1.{@link AnnotationConfigServletWebServerApplicationContext#postProcessBeanFactory}:注解方式启动时,调用该子类的方法
 * 2.{@link ClassPathBeanDefinitionScanner#scan()}:不管是xml启动还是注解启动,都会调用该方法进行包扫描处理 ComponentScan 注解,
 * 		并将扫描后的类注入到spring容器中.用法参照{@link AnnotationConfigApplicationContext#scan(String...)}
 * 3.{@link AnnotationConfigUtils#registerAnnotationConfigProcessors()}
 * ->3.1.{@link RootBeanDefinition}:以各种BeanPostProcessor实现类为构造参数,判断加载Configuration,Import,Component,ComponentScan等
 * ->3.1.1.{@link ConfigurationClassPostProcessor}:判断解析Configuration注解,Importor实现类,扫描{@link ComponentScan}所在包
 * ->3.1.1.1.{@link ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry()}
 * ->3.1.1.2.{@link ConfigurationClassPostProcessor#processConfigBeanDefinitions()}
 * ->3.1.1.3.{@link ConfigurationClassUtils#checkConfigurationClassCandidate()}:判断是否为Configuration,设置相关属性
 * ->3.1.1.4.{@link ConfigurationClassUtils#isConfigurationCandidate()}:判断是否为Import,Component,ComponentScan,ImportResource
 * ->3.1.2.{@link AutowiredAnnotationBeanPostProcessor}:判断解析Autowired,Value,javax.inject.Inject注解
 * ->3.1.3.{@link CommonAnnotationBeanPostProcessor}:判断解析javax.xml.ws.WebServiceRef,javax.ejb.EJB注解
 * ->3.1.4.{@link EventListenerMethodProcessor}:判断解析EventListenerFactory的实现类
 * 4.{@link AnnotationConfigUtils#registerPostProcessor()}
 * 5.{@link BeanDefinitionRegistry#registerBeanDefinition()}
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
 * 
 * -->1.获取容器中的所有Bean,依次进行初始化和创建对象,获取Bean的定义信息RootBeanDefinition判断Bean不是抽象的,是单实例的,是懒加载:
 * -->1.1.判断是否是FactoryBean,是否是实现FactoryBean接口的Bean
 * -->1.2.不是工厂Bean,则利用getBean(beanName)创建对象
 * -->1.2.1.{@link AbstractBeanFactory#getBean(String)};ioc.getBean();
 * -->1.2.2.{@link AbstractBeanFactory#doGetBean()}:先获取缓存中的单实例bean,如果获取到,说明之前被创建过.
 * 		所有创建过的都会被保存在{@link DefaultSingletonBeanRegistry#singletonObjects}中
 * -->1.2.2.1.{@link AbstractBeanFactory#getSingleton(String)}:从缓存中获得bean实例的单例对象
 * -->1.2.2.1.1.{@link DefaultSingletonBeanRegistry#getSingleton()}:获得bean实例的单例对象的实际操作类,同时解决循环依赖的问题
 * -->1.2.2.2.{@link AbstractBeanFactory#getParentBeanFactory()}:如果获取不到,先获取父容器,从父容器获取
 * -->1.2.2.3.{@link AbstractBeanFactory#markBeanAsCreated()}:标记当前bean已经被创建
 * -->1.2.2.4.获取bean的定义信息,获取当前bean依赖的其他bean,如果有则按照getBean()把依赖的bean先创建出来
 * -->1.2.2.5.{@link AbstractBeanFactory#getSingleton(String,ObjectFactory)}:如果是单例,先创建bean,再缓存bean
 * -->1.2.2.5.1.{@link AbstractAutowireCapableBeanFactory#createBean(String,RootBeanDefinition,Object[])}:启动单实例bean的创建流程
 * -->1.2.2.5.2.{@link AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation()}:让BeanPostProcessor先拦截返回的代理对象,
 * 		此处只会执行{@link InstantiationAwareBeanPostProcessor}中的方法,并不会执行所有的BeanPostProcessor
 * -->1.2.2.5.3.{@link AbstractAutowireCapableBeanFactory#doCreateBean}:如果1.2.2.5.2中没有返回代理的bean对象,则执行创建
 * -->1.2.2.5.4.{@link AbstractAutowireCapableBeanFactory#createBeanInstance}:利用工厂方法或者对象构造器创建出bean实例
 * -->1.2.2.5.5.{@link AbstractAutowireCapableBeanFactory#instantiateUsingFactoryMethod}:如果能拿到工厂方法名,则调用该方法创建bean
 * -->1.2.2.5.6.{@link AbstractAutowireCapableBeanFactory#applyMergedBeanDefinitionPostProcessors}:
 * 		获得所有的MergedBeanDefinitionPostProcessor,循环调用
 * -->1.2.2.5.7.{@link AbstractAutowireCapableBeanFactory#populateBean}:bean中的属性赋值
 * -->1.2.2.5.8.{@link AbstractAutowireCapableBeanFactory#initializeBean}:bean初始化
 * -->1.2.2.5.8.{@link AbstractAutowireCapableBeanFactory#registerDisposableBeanIfNecessary}:bean的销毁方法
 * -->1.2.2.6.{@link AbstractBeanFactory#addSingleton()}:缓存bean
 * 
 * 3.{@link SmartInitializingSingleton#afterSingletonsInstantiated}: 处理剩余的单例bean,此处有多种处理程序,包括监听(listener)等
 * ->3.1.{@link EventListenerMethodProcessor#afterSingletonsInstantiated()}:负责监听器的单例处理
 * ->3.1.1.{@link EventListenerMethodProcessor#processBean()}:只处理带有@EventListener注解的bean
 * ->3.1.2.{@link MethodIntrospector#selectMethods}:获得带有@EventListener注解的方法
 * </pre>
 * 
 * SpringBoot启动流程--{@link AbstractApplicationContext#finishRefresh}
 * 
 * <pre>
 * 1.{@link AbstractApplicationContext#finishRefresh()}:完成刷新,通知生命周期处理器lifecycleProcessor,发布ContextRefreshEvent事件
 * 2.{@link AbstractApplicationContext#initLifecycleProcessor()}:初始化和生命周期有关的后置处理器{@link LifecycleProcessor},
 * 		如果没有,则创建一个{@link DefaultLifecycleProcessor},该类可以在BeanFactory刷新和关闭时执行回调
 * 3.{@link AbstractApplicationContext#publishEvent()}:发布ContextRefreshEvent事件,告诉监听器容器已刷新完成
 * </pre>
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
 * </pre>
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
 * {@link ApplicationContextInitializer}:{@link SpringApplication#refreshContext()}之前的回调,此时bean还未初始化,IOC上下文环境还未完成,只能对容器做操作.
 * 		注入实现了该类的方法有2种:Configuration或在META-INF/spring.factories中添加该类,可参照spring-autoconfigure
 * {@link CommandLineRunner}:在容器启动成功之后的最后一个回调,该回调执行之后容器就成功启动
 * {@link ApplicationEvent}:自定义事件,需要发布的事件继承该接口
 * {@link ApplicationListener}:事件监听.可以直接在listener上添加注解或者使用上下文添加到容器中
 * 
 * {@link SpringServletContainerInitializer}:该类负责对容器启动时相关组件进行初始化,当前类只是完成一些验证和组件装配,
 * 		具体初始化由类上注解{@link HandlesTypes}的值决定,实际上就是{@link WebApplicationInitializer}的实现类.
 * #TomcatStarter:该类并非通过SPI机制实例化,因为该类非public,也无无参构造,本质上是通过new实例化的.
 * 		该类和{@link SpringServletContainerInitializer}类似,主要初始化是通过{@link ServletContextInitializer}完成
 * 
 * {@link SpringApplicationRunListener}:{@link SpringApplication#run()}调用,所有实现该接口的类都必须添加一个构造,且该构造的参数类型固定,详见{@link SelfApplicationListener}.
 * 		实现该接口的类使用@Configuration或@Component等注解无法注入到Spring上下文中,只能通过META-INF/spring.factories添加相应配置才能生效
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
 * Spring根目录下META-INF下各种文件作用,不同版本
 * 
 * <pre>
 * spring.factories:各种自动配置,监听器,初始化器等类的全路径名,由{@link EnableAutoConfiguration}注解加载
 * spring.handlers:如果需要自定义xml的命名空间,需要写在该文件中,需要实现接口{@link NamespaceHandler},自定义处理xml
 * spring.schemas:各种xml的xsd不同版本的文件格式约束映射关系以及xsd文件在包中的位置
 * spring-configuration-metadata.json:{@link ConfigurationProperties}修饰的类编译后产生的自定义属性提示文件
 * services/javax.servlet.ServletContainerInitializer:Servlet容器启动时扫描的 ServletContainerInitializer 实现类,见{@link SelfServletContainerInitializer}
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
 * Spring对配置类的处理主要分为2个阶段:
 * 
 * <pre>
 * 1.配置类解析阶段:会得到一批配置类的信息,和一些需要注册的bean
 * 类中有下面任意注解之一的就属于配置类:{@link Component}, {@link Configuration}, {@link ComponentScan}, {@link Import}, {@link ImportResource}, {@link Bean}
 * org.springframework.context.annotation.ConfigurationClassUtils#isConfigurationCandidate:判断一个类是不是一个配置类
 * 
 * 2.bean注册阶段:将配置类解析阶段得到的配置类和需要注册的bean注册到spring容器中
 * 
 * Spring中处理这2个过程会循环进行,直到完成所有配置类的解析及所有bean的注册
 * {@link ConfigurationClassPostProcessor#processConfigBeanDefinitions(BeanDefinitionRegistry)}:Spring对配置类处理过程
 * 
 * 整个过程大致的过程如下:
 * 1.通常我们会通过new AnnotationConfigApplicationContext()传入多个配置类来启动spring容器
 * 2.Spring对传入的多个配置类进行解析
 * 3.配置类解析阶段:这个过程就是处理配置类上面6中注解的过程,此过程中又会发现很多新的配置类.比如@Import导入的一批新的类刚好也符合配置类,
 * 	而被@CompontentScan扫描到的一些类刚好也是配置类;此时会对这些新产生的配置类进行同样的过程解析
 * 4.Bean注册阶段:配置类解析后,会得到一批配置类和一批需要注册的bean,此时spring容器会将这批配置和这批需要注册的bean注册到spring容器
 * 5.经过上面第3个阶段之后,spring容器中会注册很多新的bean,这些新的bean中可能又有很多新的配置类
 * 6.Spring从容器中将所有bean拿出来,遍历一下,会过滤得到一批未处理的新的配置类,继续交给第3步进行处理
 * 7.step3到step6,这个过程会经历很多次,直到完成所有配置类的解析和bean的注册
 * 
 * 从上面过程中可以了解到:
 * 1.可以在配置类上面加上{@link Conditional},来控制是否需要解析这个配置类,配置类如果不被解析,那么这个配置上面6种注解的解析都会被跳过
 * 2.可以在被注册的bean上面加上{@link Conditional},来控制这个bean是否需要注册到spring容器中
 * 3.如果配置类不会被注册到容器,那么这个配置类解析所产生的所有新的配置类及所产生的所有新的bean都不会被注册到容器
 * </pre>
 * 
 * 在被{@link Configuration}修饰的类中用{@link Bean}创建对象和直接用{@link Bean}创建对象的区别
 * 
 * <pre>
 * 使用Configuration的该类默认情况下会CgLib代理,其中创建的bean默认是单例
 * 不使用Configuration的类不会注入到Spring中,使用底层代理,bean默认是原型
 * 
 * {@link ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry}
 * {@link ConfigurationClassPostProcessor#postProcessBeanFactory}
 * {@link ConfigurationClassPostProcessor#enhanceConfigurationClasses}
 * </pre>
 * 
 * 一些实用方法:
 * 
 * <pre>
 * {@link ClassUtils#isPresent(String, ClassLoader)}:判断某个类是否存在,不限定在Spring上下文中
 * {@link ListableBeanFactory#containsBeanDefinition(String)}:判断指定名称的bean是否已定义
 * {@link ListableBeanFactory#getBeanNamesForType(Class)}:判断指定class的bean是否已定义
 * {@link SingletonBeanRegistry#registerSingleton()}:通过{@link DefaultSingletonBeanRegistry}注册单例的bean到Spring上下文中
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