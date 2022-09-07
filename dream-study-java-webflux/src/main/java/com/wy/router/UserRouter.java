package com.wy.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 使用路由方式操作数据库
 *
 * @author 飞花梦影
 * @date 2022-09-02 10:06:51
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
public class UserRouter {

	@Bean
	public RouterFunction<ServerResponse> customRouter(UserHandler handler) {
		return RouterFunctions.nest(
				// 前端请求前缀,相当于server.context-path
				RequestPredicates.path("/user"),
				// 一个请求:请求URL值;请求处理的方法,相当于service层
				RouterFunctions.route(RequestPredicates.GET("/findAll"), handler::findAll)
						// 添加多个请求
						.andRoute(RequestPredicates.POST("/save")
								.and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::saveValid)
						.andRoute(RequestPredicates.DELETE("/delete/{id}"), handler::delete)
						.andRoute(RequestPredicates.PUT("/update/{id}"), handler::update));
	}
}