package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Profile;

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
 * @author 飞花梦影
 * @date 2020-12-02 15:16:40
 * @git {@link https://github.com/mygodness100}
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