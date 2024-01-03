package com.wy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-01-03 16:46:48
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		// 允许cookies跨域
		corsConfiguration.setAllowCredentials(true);
		// 允许向该服务器提交请求的URI,*表示全部允许,在SpringMVC中,如果设成*,会自动转成当前请求头中的Origin
		corsConfiguration.addAllowedOriginPattern("*");
		// corsConfiguration.addAllowedOrigin("*");
		// 允许访问的头信息,*表示全部
		corsConfiguration.addAllowedHeader("*");
		// 允许自定义的头部,大小写不敏感
		// corsConfiguration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers",
		// "Accept", "Origin",
		// "Content-Disposition", "Authorization", "No-Cache", "X-Requested-With",
		// "If-Modified-Since", "Pragma",
		// "Last-Modified", "Cache-Control", "Expires", "Content-Type", "X-E4M-With",
		// "userId", "token"));
		// 预检请求的缓存时间(秒),即在这个时间段里,对于相同的跨域请求不会再预检了
		corsConfiguration.setMaxAge(3600L);
		// 允许提交请求的方法类型,*表示全部允许
		corsConfiguration.addAllowedMethod("OPTIONS");
		corsConfiguration.addAllowedMethod("HEAD");
		corsConfiguration.addAllowedMethod("GET");
		corsConfiguration.addAllowedMethod("PUT");
		corsConfiguration.addAllowedMethod("POST");
		corsConfiguration.addAllowedMethod("DELETE");
		corsConfiguration.addAllowedMethod("PATCH");

		// 是否允许请求带有验证信息
		corsConfiguration.setAllowCredentials(true);

		// 允许脚本访问的返回头
		// corsConfiguration.setHeader("Access-Control-Expose-Headers", arg1);
		return corsConfiguration;
	}

	@Bean
	CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig());
		return new CorsFilter(source);
	}
}