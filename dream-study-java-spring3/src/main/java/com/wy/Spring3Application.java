package com.wy;

import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.wy.initializer.SelfServletContainerInitializer;

/**
 * SpringBoot3项目resources/META-INF下各种文件作用,和版本SpringBoot2不一样
 * 
 * <pre>
 * spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports:代替spring.factories,各种自动配置,监听器,初始化器等类的全路径名,由{@link EnableAutoConfiguration}注解加载
 * spring.handlers:如果需要自定义xml的命名空间,需要写在该文件中,需要实现接口{@link NamespaceHandler},自定义处理xml
 * spring.schemas:各种xml的xsd不同版本的文件格式约束映射关系以及xsd文件在包中的位置
 * spring-configuration-metadata.json:{@link ConfigurationProperties}修饰的类编译后产生的自定义属性提示文件
 * services/javax.servlet.ServletContainerInitializer:Servlet容器启动时扫描的 ServletContainerInitializer 实现类,见{@link SelfServletContainerInitializer}
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-10-10 16:39:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class Spring3Application {

	public static void main(String[] args) {
		SpringApplication.run(Spring3Application.class, args);
	}
}