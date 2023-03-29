package com.wy.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.netty.http.server.HttpServer;

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
								// 设置接收的请求头类型
								.and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::saveValid)
						.andRoute(RequestPredicates.DELETE("/delete/{id}"), handler::delete)
						.andRoute(RequestPredicates.PUT("/update/{id}"), handler::update));
	}

	/**
	 * 手动创建服务器完成适配,用来测试
	 * 
	 * @param handler
	 */
	public void createReactorServer(UserHandler handler) {
		// 路由和handier适配
		RouterFunction<ServerResponse> route = customRouter(handler);
		HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
		ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
		// 创建服务器
		HttpServer httpServer = HttpServer.create();
		httpServer.handle(adapter).bindNow();
	}
}