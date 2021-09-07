package com.wy;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * SpringBoot学习:初始化initialize,listener,自动配置,配置文件.
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
 * 23.{@link SpringApplication#refreshContext}:调用context的registerShutdownHook(),执行context的refresh(),根据不同环境执行不同的refresh(),
 * 		最终调用{@link AbstractApplicationContext#refresh()},注册各种Bean以及Spring组件
 * 24.{@link SpringApplication#afterRefresh}:用户可自定义加载完成的方法
 * 25.执行所有的{@link SpringApplicationRunListener#started()}
 * 26.{@link SpringApplication#callRunners}:回调,获取容器中所有的{@link ApplicationRunner},{@link CommandLineRunner},依次调用
 * 27.执行所有的{@link SpringApplicationRunListener#running()}
 * 28.返回{@link ConfigurableApplicationContext}
 * </pre>
 * 
 * {@link SpringApplication#refreshContext()}
 * ->{@link AbstractApplicationContext#refresh()}
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
 *
 * {@link AutowiredAnnotationBeanPostProcessor}:加载由Autowired和Value注解修饰的成员变量,支持{@link Inject},由{@link BeanUtils#instantiateClass}实例化
 * {@link AnnotationConfigApplicationContext},{@link AnnotationConfigWebApplicationContext}:根据环境不同启动加载{@link Configuration}
 * 
 * {@link ApplicationContextInitializer}:在spring调用refreshed方法之前调用该方法.是为了对spring容器做进一步的控制
 * 注入实现了该类的方法有2种:Configuration或者在META-INF的spring.factories中添加该类,可参照spring-autoconfigure包里的添加
 * {@link CommandLineRunner}:在容器启动成功之后的最后一个回调,该回调执行之后容器就成功启动
 * {@link ApplicationEvent}:自定义事件,需要发布的事件继承该接口
 * {@link ApplicationListener}:事件监听.可以直接在listener上添加注解或者使用上下文添加到容器中
 * {@link publishEvent}:发布事件,必须在refreshed之后调用.使用任何继承了上下文的context调用,传入ApplicationEvent
 * 
 * @author 飞花梦影
 * @date 2020-12-02 15:16:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
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