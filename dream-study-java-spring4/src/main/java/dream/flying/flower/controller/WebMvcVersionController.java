//package dream.flying.flower.controller;
//
//import org.jspecify.annotations.Nullable;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.web.accept.ApiVersionResolver;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import jakarta.servlet.http.HttpServletRequest;
//
///**
// * 
// *
// * @author 飞花梦影
// * @date 2025-12-18 14:12:11
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//@RestController
//@RequestMapping("test-web-mvc")
//public class WebMvcVersionController {
//
//	@GetMapping(value = "test1", version = "1")
//	public void test1() {
//		System.out.println("test1.1");
//	}
//
//	@GetMapping(value = "test1", version = "2")
//	public void test2() {
//		System.out.println("test1.2");
//	}
//}
//
///**
// * 配置版本控制使用策略
// *
// * @author 飞花梦影
// * @date 2025-12-18 14:20:43
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//@Configuration
//class WebMvcConfiguration implements WebMvcConfigurer {
//
//	@Override
//	public void configureApiVersioning(ApiVersionConfigurer configurer) {
//		// 方式 1:使用请求参数（默认参数名为 "version"）
//		configurer.useQueryParam("version");
//
//		// 方式 2:使用请求头
//		// configurer.useRequestHeader("api-version");
//
//		// 方式 3:使用路径变量
//		// configurer.usePathSegment("version");
//
//		// 方式4:使用请求头中的某个参数:Accept: application/vnd.api+json;version=1
//		configurer.useMediaTypeParameter(MediaType.APPLICATION_JSON, "version");
//
//		// 方式5:自定义版本控制
//		configurer.useVersionResolver(new ApiVersionResolver() {
//
//			@Override
//			@Nullable
//			public String resolveVersion(HttpServletRequest request) {
//				// 从用户代理字符串解析版本
//				String userAgent = request.getHeader("User-Agent");
//				if (userAgent != null && userAgent.contains("mobile")) {
//					return "mobile";
//				}
//
//				// 基于客户端 IP 或其他业务规则
//				// ....
//
//				// 默认版本
//				return "1";
//			}
//		});
//	}
//}