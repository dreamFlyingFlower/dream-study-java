package com.wy.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ParameterType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 使用Swagger2自动生成文档,文档查看地址http://localhost:8810/swagger-ui.html#/
 * {@link http://www.leftso.com/blog/402.html}
 * 
 * 在使用的时候,swagger-annotations和swagger-models要用1.5.21版本的,否则会有问题
 * 
 * 若字段以大写开头,则需要加上jackjson其他2个注解,否则swagger2将无法显示字段注释
 * JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class):字段以大写开头
 * JsonAutoDetect(fieldVisibility = Visibility.ANY):检测所有修饰符字段
 * 
 * @author 飞花梦影
 * @date 2020-12-08 11:29:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@EnableSwagger2
@Deprecated
public class Swagger2Config {

	@Bean
	public Docket createRestApi() {

		// swagger2添加公共请参数,在swagger3中已经废除
		ParameterBuilder token = new ParameterBuilder();
		ParameterBuilder tenant = new ParameterBuilder();
		token.name("X-Auth-Token").description("访问令牌").modelRef(new ModelRef("string")).parameterType("header")
		        .required(false).build();
		tenant.name("X-Auth-Tenant").description("租户编码").modelRef(new ModelRef("string")).parameterType("header")
		        .required(false).build();
		List<Parameter> pars = new ArrayList<>();
		pars.add(token.build());
		pars.add(tenant.build());

		// 3.0添加请求,可能不对
		RequestParameterBuilder tokenBuilder = new RequestParameterBuilder();
		tokenBuilder.name("token").in(ParameterType.HEADER).required(false);
		return new Docket(DocumentationType.SWAGGER_2).groupName("通用文档").apiInfo(apiInfo()).select()
		        .apis(RequestHandlerSelectors.basePackage("com.wy.crl")).paths(PathSelectors.any()).build()
		        .globalOperationParameters(pars)
		        // 忽略某个类,可连写多个
		        .ignoredParameterTypes(Swagger2Config.class);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("接口文档").description("通用接口文档").version("1.0").build();
	}
}