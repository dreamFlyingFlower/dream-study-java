package com.wy.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;

/**
 * 注入原生的ES高级 API
 *
 * @author 飞花梦影
 * @date 2022-07-16 17:31:00
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
// @Configuration
public class EsConfig {

	@Bean
	public RestHighLevelClient restHighLevelClient() {
		// 从配置文件中取得es集群地址
		HttpHost httpHost = new HttpHost("192.168.1.150", 9876);
		return new RestHighLevelClient(RestClient.builder(httpHost));
	}
}