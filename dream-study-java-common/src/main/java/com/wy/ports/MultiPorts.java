package com.wy.ports;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 同一个服务中同时监听多个端口,但是服务仍然是同样的一个,主要可以用来同时跑https和http
 *
 * @author 飞花梦影
 * @date 2024-10-09 14:10:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MultiPorts {

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		// 配置主端口8080
		factory.setPort(8080);
		// 添加额外的Connector监听8081端口
		Connector additionalConnector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		additionalConnector.setPort(8081);
		factory.addAdditionalTomcatConnectors(additionalConnector);
		// 可添加更多Connector监听不同端口
		return factory;
	}
}