package dream.study.spring.config;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * 读取配置文件中的信息
 *
 * {@link Configuration}:将该注解修改的类注入到Spring的组件中,和{@link Component}差不多,只不过语义不一样
 * 
 * 主要配置文件:
 * 
 * <pre>
 * bootstrap.yml:程序启动时必然读取,该配置文件主要是为了微服务中的自动配置服务而使用,用于加载application等配置文件使用
 * application.yml:若没有配置微服务的远程自动配置服务,必然读取该文件,后于bootstrap.yml读取
 * application-env.yml:env可以是任意自定义名称,需要在application中引入,否则将不读取
 * </pre>
 * 
 * {@link ConfigurationProperties}:该类中的属性可以在配置文件中自动提示,这些自定义属性最好是配置在application[-env].yml中,
 * 配置在bootstrap.yml中可能不会读取.和{@link Configuration}配合使用,Spring启动时会自动引入配置信息
 * {@link EnableConfigurationProperties}:该注解已经在{@link EnableAutoConfiguration}中自动注入,不需要手动显示在类上配置.
 * 该注解可以自动将ConfigurationProperties修改的配置类注入到其他使用该配置类的Bean中
 * {@link ConfigurationPropertiesBindingPostProcessor}:该类实现了BeanPostProcessor,在postProcessBeforeInitialization()中
 * 调用了{@link ConfigurationPropertiesBean#create()}解析{@link ConfigurationProperties},
 * 最终调用 #ConfigurationPropertiesBinder的bind()绑定数据到相应的对象上
 * 
 * {@link PropertySource}:需要和ConfigurationProperties配合使用,读取指定地址的配置,但只能是properties文件
 * 若yml和properties有相同配置,以properties优先使用,properties中没有的才读取yml
 * 
 * {@link Value}:可以直接读取配置文件中的属性,不需要在上ConfigurationProperties和PropertySource注解中定义
 * {@link Validated}:对类中的属性进行校验,如email,若有值则必须是一个email,否则报错,需要配合ConfigurationProperties使用.
 * 如果配置中嵌套了其他对象,该对象必须有{@link Valid}进行注解
 * 
 * 配置文件中属性的值可以使用占位符,即使用配置文件中已经存在的其他属性.如${server.port}.
 * 也可以使用{@link RandomValuePropertySource}中的属性赋值,如config.user-id=${random.uuid}
 * 
 * {@link ImportResource}:导入spring的xml配置文件,类似的xml配置可使用{@link Bean}代替
 * 
 * 详见{@link TestConfig#testConfig},application-config.yml
 * 
 * @author 飞花梦影
 * @date 2020-12-02 13:46:15
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
@ConfigurationProperties(prefix = "config")
@Validated
@PropertySource("classpath:person.properties")
@Getter
@Setter
public class ConfigProperties {

	private String userId;

	private Integer age;

	private Date birthday;

	private Boolean man;

	@Email
	private String email;

	private String placeholder;

	@Valid
	private Inner inner;

	@Getter
	@Setter
	public static class Inner {

		private String name;
	}
}