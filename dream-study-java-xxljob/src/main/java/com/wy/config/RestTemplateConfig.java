package com.wy.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate
 *
 * @author 飞花梦影
 * @date 2022-01-04 17:29:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@ConditionalOnMissingBean(RestTemplate.class)
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}