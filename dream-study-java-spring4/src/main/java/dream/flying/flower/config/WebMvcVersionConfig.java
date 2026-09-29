package dream.flying.flower.config;

import java.net.URI;
import java.time.ZonedDateTime;

import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ApiVersionDeprecationHandler;
import org.springframework.web.accept.ApiVersionParser;
import org.springframework.web.accept.ApiVersionResolver;
import org.springframework.web.accept.ApiVersionStrategy;
import org.springframework.web.accept.MediaTypeParamApiVersionResolver;
import org.springframework.web.accept.SemanticApiVersionParser;
import org.springframework.web.accept.StandardApiVersionDeprecationHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 版本控制配置,也可以使用配置文件.需要在{@link GetMapping#version()}中指定版本,不指定则使用默认
 *
 * 参考{@link https://docs.spring.io/spring/reference/web/webflux-versioning.html}
 * 
 * <pre>
 * {@link ApiVersionResolver}: API版本解析方式,有4种默认实现,可自定义,实现{@link WebFluxConfigurer}即可
 * ->{@link MediaTypeParamApiVersionResolver}: 该方式先从ACCEPT中取指定属性,如果有,解析后取属性值;如果没有,从Content-Type中取指定属性
 * {@link ApiVersionStrategy}: API版本访问策略,根据所有ApiVersionResolver实现类进行逐一解析,选出匹配的解析器
 * {@link ApiVersionParser}: API版本解析,默认{@link SemanticApiVersionParser},只匹配数字和小数
 * {@link ApiVersionDeprecationHandler}: 配置里声明某个版本即将废弃,框架会在响应头里自动加上 Deprecation、Sunset、Link 等信息
 * ->{@link StandardApiVersionDeprecationHandler}: 标准的版本废弃处理方法
 * </pre>
 *
 * @author 飞花梦影
 * @date 2025-12-18 14:43:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class WebMvcVersionConfig implements WebMvcConfigurer {

	@Override
	public void configureApiVersioning(ApiVersionConfigurer configurer) {
		// 方式 1:使用请求参数（默认参数名为 "version"）
		configurer.useQueryParam("version");

		// 方式 2:使用请求头
		// configurer.useRequestHeader("X-API-Version");

		// 方式 3:使用路径变量
		// configurer.usePathSegment("version");

		// 方式4:使用请求头中的某个参数:Accept: application/vnd.api+json;version=1
		configurer.useMediaTypeParameter(MediaType.APPLICATION_JSON, "version");

		// 方式5:自定义版本控制
		configurer.useVersionResolver(new ApiVersionResolver() {

			@Override
			@Nullable
			public String resolveVersion(HttpServletRequest request) {
				// 从用户代理字符串解析版本
				String userAgent = request.getHeader("User-Agent");
				if (userAgent != null && userAgent.contains("mobile")) {
					return "mobile";
				}

				// 基于客户端 IP 或其他业务规则
				// ....

				// 默认版本
				return "1";
			}
		});
		// 添加支持的版本
		configurer.addSupportedVersions("1", "2");

		// 废弃版本,配合版本控制,会自动在响应中添加Deprecation,Sunset,Link字段
		StandardApiVersionDeprecationHandler handler = new StandardApiVersionDeprecationHandler();

		// 标记 v1 即将废弃,并设置下线时间
		handler.configureVersion("1")
				.setDeprecationDate(ZonedDateTime.parse("2026-06-01T00:00:00Z"))
				.setSunsetDate(ZonedDateTime.parse("2026-12-31T00:00:00Z"))
				.setSunsetLink(URI.create("https://docs.example.com/migrate-v2"));
		// 添加废弃版本处理
		configurer.setDeprecationHandler(handler);
	}
}