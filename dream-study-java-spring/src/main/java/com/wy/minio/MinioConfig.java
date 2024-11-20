package com.wy.minio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

/**
 * MinioClient
 *
 * @author 飞花梦影
 * @date 2023-06-20 15:55:44
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Configuration
public class MinioConfig {

	@Bean
	MinioClient minioClient(MinioProperties minioProperties) {
		return MinioClient.builder().endpoint(minioProperties.getEndpoint())
				.credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()).build();
	}
}