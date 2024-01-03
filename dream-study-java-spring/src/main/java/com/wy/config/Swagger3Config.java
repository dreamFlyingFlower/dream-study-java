package com.wy.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 使用Swagger3自动生成文档,文档查看地址http://ip:port/swagger-ui/index.html
 * {@link http://www.leftso.com/blog/402.html}
 * 
 * 若字段以大写开头,则需要加上jackjson其他2个注解,否则swagger将无法显示字段注释
 * JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class):字段以大写开头
 * JsonAutoDetect(fieldVisibility = Visibility.ANY):检测所有修饰符字段
 * 
 * @author 飞花梦影
 * @date 2020-12-08 11:29:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@EnableOpenApi
public class Swagger3Config {

	@Bean
	Docket generateApi() {
		// springfox3添加公共请求头
		List<RequestParameter> requestParameters = new ArrayList<>();
		requestParameters.add(new RequestParameterBuilder().name("token").description("token").in(ParameterType.HEADER)
				.required(false).build());

		return new Docket(DocumentationType.OAS_30).groupName("API文档").apiInfo(apiInfo()).select()
				// 扫描指定包路径,只能写一个,不支持匹配正则
				.apis(RequestHandlerSelectors.basePackage("com.wy.crl"))
				// 扫描标识有指定注解的类
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				// 扫描标识有指定注解的方法
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any())
				.build()
				// 全局请求字段
				.globalRequestParameters(requestParameters)
				// 忽略某个类,可连写多个
				.ignoredParameterTypes(Swagger3Config.class);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("接口文档").description("通用接口文档").version("1.0").build();
	}
}