package com.wy.template;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * HttpClient升级到5.0,且groupId有变化,直接影响RestTemplate
 *
 * @author 飞花梦影
 * @date 2023-12-29 15:35:00
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Configuration
public class MyRestTemplate {

	@SuppressWarnings("resource")
	@Bean
	RestTemplate restTemplate() {

		DefaultClientTlsStrategy defaultClientTlsStrategy = DefaultClientTlsStrategy.createDefault();

		final PoolingHttpClientConnectionManager manager = PoolingHttpClientConnectionManagerBuilder.create()
				.setTlsSocketStrategy(defaultClientTlsStrategy)
				.build();

		final CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(manager).build();

		final HttpComponentsClientHttpRequestFactory componentsClientHttpRequestFactory =
				new HttpComponentsClientHttpRequestFactory();
		componentsClientHttpRequestFactory.setHttpClient(closeableHttpClient);

		final RestTemplate restTemplate = new RestTemplate(componentsClientHttpRequestFactory);
		return restTemplate;
	}
}