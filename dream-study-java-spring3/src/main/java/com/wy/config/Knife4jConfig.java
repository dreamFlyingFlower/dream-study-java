package com.wy.config;

import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * 自定义Knife4j文档,也可以直接在配置中编写,但是有些属性在配置文件中没有
 *
 * @author 飞花梦影
 * @date 2025-04-09 23:22:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class Knife4jConfig {

	@Bean
	GroupedOpenApi api2() {
		return GroupedOpenApi.builder()
				// 分组标识,最好不要有中文,可能出错
				.group("01-business-api")
				// 分组展示名称
				.displayName("01-业务接口")
				// 扫描包路径
				.packagesToScan("cn.keyidea.business")
				// 匹配URL路径
				.pathsToMatch("/v1/**")
				// 过滤
				.addOpenApiMethodFilter(method ->
				// 方法上需要有Operation注解才扫描
				method.isAnnotationPresent(io.swagger.v3.oas.annotations.Operation.class))
				// 自定义全局响应码
				.addOpenApiCustomizer((this::setCustomStatusCode))
				.build();
	}

	/**
	 * 设置自定义错误码
	 *
	 * @param openApi openApi对象
	 */
	private void setCustomStatusCode(OpenAPI openApi) {
		if (openApi.getPaths() != null) {
			Paths paths = openApi.getPaths();
			for (Map.Entry<String, PathItem> entry : paths.entrySet()) {
				PathItem value = entry.getValue();
				// put方式自定义全局响应码
				Operation put = value.getPut();
				// get方式自定义全局响应码
				Operation get = value.getGet();
				// delete方式自定义全局响应码
				Operation delete = value.getDelete();
				// post方式自定义全局响应码
				Operation post = value.getPost();
				if (put != null) {
					put.setResponses(handleResponses(put.getResponses()));
				}
				if (get != null) {
					get.setResponses(handleResponses(get.getResponses()));
				}
				if (delete != null) {
					delete.setResponses(handleResponses(delete.getResponses()));
				}
				if (post != null) {
					post.setResponses(handleResponses(post.getResponses()));
				}
			}
		}
	}

	/**
	 * 处理不同请求方式中的自定义响应码
	 * 
	 * 响应码中使用原有的响应体Content,否则会造成BaseRes中通用的data无法解析各自的对象
	 * 
	 * 使用原生的ApiResponses作为返回体,否则会造成前端响应示例和响应内容中丢失注释
	 *
	 * @param responses 响应体集合
	 * @return 返回处理后的响应体集合
	 */
	private ApiResponses handleResponses(ApiResponses responses) {
		// 设置默认Content
		Content content = new Content();
		// 以下代码注释,因为无论如何都会从原生responses中获取到一个Content
		// MediaType mediaType = new MediaType();
		// Schema schema = new Schema();
		// schema.set$ref("#/components/schemas/BaseRes");
		// mediaType.setSchema(schema);
		// content.addMediaType("*/*", mediaType);

		// 从原来的responses中获取原生Content
		for (Map.Entry<String, ApiResponse> entry : responses.entrySet()) {
			ApiResponse apiResponse = entry.getValue();
			if (apiResponse != null) {
				content = apiResponse.getContent();
				break;
			}
		}

		// 获取全部全局响应自定义列表,需要自定义响应码
		Map<Integer, String> map = new HashMap<>();
		// 设置全局响应码
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			ApiResponse api = new ApiResponse();
			api.setContent(content);
			api.description(entry.getValue());
			responses.addApiResponse(entry.getKey() + "", api);
		}
		return responses;
	}

	@Bean
	OpenAPI openAPI() {
		return new OpenAPI().info(new Info().title("文档标题")
				.description("文档描述")
				.version("文档版本")
				.contact(new Contact().name("DreamFlyingFlower").email("123456789@qq.com"))
				.license(new License().name("许可名称").url("许可地址")));
	}
}