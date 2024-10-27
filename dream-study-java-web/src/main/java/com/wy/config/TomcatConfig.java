package com.wy.config;

import java.io.File;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 自定义配置Tomcat参数,配置https访问
 * 
 * JDK生成密钥证书:keytool -genkey -alias 别名 -storetype 仓库类型 -keyalg 算法 -keysize 长度
 * -keystore 文件名 -validity 有效期 仓库类型:JKS,JCEKS,PKCS12等 算法:RSA,DSA 长度:例如2048
 *
 * @author 飞花梦影
 * @date 2021-09-27 11:26:25
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
public class TomcatConfig {

	@Bean
	ServletWebServerFactory servletWebServerFactory() {
		TomcatServletWebServerFactory webServerFactory = new TomcatServletWebServerFactory();
		webServerFactory.addAdditionalTomcatConnectors(buildConnector());
		return webServerFactory;
	}

	private Connector buildConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		Http11NioProtocol httpProtocol = (Http11NioProtocol) connector.getProtocolHandler();
		try {
			File keyStoreFile = new ClassPathResource("keystore").getFile();
			File trustStoreFile = new ClassPathResource("keystore").getFile();
			connector.setScheme("https");
			connector.setSecure(true);
			connector.setPort(443);
			httpProtocol.setSSLEnabled(true);
			httpProtocol.setKeystoreFile(keyStoreFile.getAbsolutePath());
			httpProtocol.setKeystorePass("password");
			httpProtocol.setTruststoreFile(trustStoreFile.getAbsolutePath());
			httpProtocol.setTruststorePass("password");
			httpProtocol.setKeyAlias("alias");
			return connector;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}