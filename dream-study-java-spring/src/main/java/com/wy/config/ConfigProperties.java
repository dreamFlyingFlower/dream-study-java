package com.wy.config;

import java.util.Date;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * 读取配置文件中的信息
 *
 * {@link @Configuration}:将该类注入到Spring的组件中<br>
 * 
 * {@link @ConfigurationProperties}:该类中的属性可以在配置文件中自动提示,<br>
 * 默认读取bootstrap.yml和application.yml中的配置信息,若需要读取其他yml中的信息,需要在上述2个文件中引入<br>
 * 和第一个注解配合使用,Spring启动时会自动引入配置信息,在其他组件中使用配置信息时,注入当前组件即可
 * 
 * {@link @PropertySource}:需要和ConfigurationProperties}配合使用,读取指定地址的配置,但只能是properties文件
 * 若yml和properties有相同配置,以properties优先使用,properties中没有的才读取yml
 * 
 * 若是没有在配置类中的属性,可以直接通过{@link Value}注解来读取,也可以获取使用了以上2个注解的属性
 * 
 * {@link @Validated}:可以对类中的属性进行校验,如email,若有值则必须是一个email,否则报错
 * 
 * 配置文件中属性的值可以使用占位符,即使用配置文件中已经存在的其他属性.<br>
 * 也可以使用{@link RandomValuePropertySource}中的属性赋值,如config.user-id=${random.uuid}
 * 
 * 详见{@link TestConfig#testConfig},application-config.yml
 * 
 * @author ParadiseWY
 * @date 2020-12-02 13:46:15
 * @git {@link https://github.com/mygodness100}
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
}