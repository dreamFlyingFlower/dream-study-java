package dream.flying.flower.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ApiVersionConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * 版本控制配置
 *
 * @author 飞花梦影
 * @date 2025-12-18 14:43:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class WebFluxVersionConfig implements WebFluxConfigurer {

	@Override
	public void configureApiVersioning(ApiVersionConfigurer configurer) {
		// 方式 1:使用请求参数（默认参数名为 "version"）
		configurer.useQueryParam("version");

		// 方式 2:使用请求头
		// configurer.useRequestHeader("api-version");

		// 方式 3:使用路径变量
		// configurer.usePathSegment("version");

		// 方式4:使用请求头中的某个参数:Accept: application/vnd.api+json;version=1
		// configurer.useMediaTypeParameter(MediaType.APPLICATION_JSON, "version");

		// 方式5:自定义版本控制
		// configurer.useVersionResolver(new ApiVersionResolver() {
		//
		// @Override
		// @Nullable
		// public String resolveVersion(ServerWebExchange exchange) {
		// // 从用户代理字符串解析版本
		//
		// HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
		//
		// String userAgent = httpHeaders.getFirst("User-Agent");
		// if (userAgent != null && userAgent.contains("mobile")) {
		// return "mobile";
		// }
		//
		// // 基于客户端 IP 或其他业务规则
		// // ....
		//
		// // 默认版本
		// return "1";
		// }
		// });
	}
}