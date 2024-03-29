package com.wy.config;

import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import com.dream.collection.ListHelper;

import dream.flying.flower.autoconfigure.web.properties.OpenApiProperties;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.AllArgsConstructor;

/**
 * OpenOpi添加全局请求参数
 *
 * @author 飞花梦影
 * @date 2023-08-14 10:21:31
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Configuration
@EnableConfigurationProperties(OpenApiProperties.class)
@AllArgsConstructor
public class OpenApiConfig implements GlobalOperationCustomizer {

	private final OpenApiProperties openApiProperties;

	/**
	 * 给某个组添加参数
	 */
	@Bean
	GroupedOpenApi groupedOpenApi() {
		return GroupedOpenApi.builder().group("鉴权").pathsToMatch("/**")
				.addOperationCustomizer((operation, handlerMethod) -> {
					return operation
							.addParametersItem(new HeaderParameter().name("token").description("token校验").required(true)
									.schema(new StringSchema()._default("token").name("token").description("token校验")));
				}).build();
	}

	/**
	 * 添加全局参数
	 * 
	 * @param operation 操作
	 * @param handlerMethod 方法
	 * @return 操作
	 */
	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		if (ListHelper.isEmpty(openApiProperties.getGlobalParameters())) {
			return operation;
		}

		List<Parameter> globalParameters = openApiProperties.getGlobalParameters();
		for (Parameter globalParameter : globalParameters) {
			operation.addParametersItem(globalParameter);
		}

		return operation;
	}
}