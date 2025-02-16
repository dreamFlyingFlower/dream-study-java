package com.wy.config;

import java.time.Duration;

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

/**
 * RestClient配置,替代RestTemplate
 *
 * @author 飞花梦影
 * @date 2025-02-11 15:12:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyRestClientConfig {

	@Bean
	RestClient restClient() {
		ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.defaults()
				.withConnectTimeout(Duration.ofSeconds(1))
				.withReadTimeout(Duration.ofSeconds(1));
		return RestClient.builder().requestFactory(ClientHttpRequestFactoryBuilder.detect().build(settings)).build();
	}
}