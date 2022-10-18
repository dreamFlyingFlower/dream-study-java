//package com.wy.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import com.wy.interceptor.FirstInterceptor;
//
///**
// * 添加interceptor到web容器中,可以继承{@link WebMvcConfigurationSupport}或实现{@link WebMvcConfigurer}
// * 若继承WebMvcConfigurationSupport可能会造成Jackson的自定义配置失效,最好实现WebMvcConfigurer
// * 
// * @author 飞花梦影
// * @date 2021-02-02 16:00:43
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//@Configuration
//public class InterceptorConfig extends WebMvcConfigurationSupport {
//
//	@Autowired
//	private FirstInterceptor firstInterceptor;
//
//	@Override
//	protected void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(firstInterceptor)
//				// 拦截的请求,只对单个拦截器有效,每个拦截都需要配置,不配置默认拦截所有请求
//				.addPathPatterns("/**")
//				// 不拦截的请求
//				.excludePathPatterns("/bigscreen/template1/js/jquery.easyui.min.js", "/static/**",
//						"/bigscreen/template1/js/*.js", "/bigscreen/tempate1/**", "/bigscreen/**",
//						"/bigscreen/template1/js/**");
//		// 若interceptor没有加入到springboot上下文中,可以直接new一个,效果同上
//		// registry.addInterceptor(new FirstInterceptor());
//	}
//}