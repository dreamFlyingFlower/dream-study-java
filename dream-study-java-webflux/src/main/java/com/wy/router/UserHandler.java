package com.wy.router;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.wy.model.User;
import com.wy.repository.UserRepository;

import reactor.core.publisher.Mono;

@Component
public class UserHandler {

	// 无效姓名列表
	private static final String[] INVALIDE_NAMES = { "admin", "administrator", "xxx", "ooo" };

	@Autowired
	private UserRepository userRepository;

	public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
		return ServerResponse
		        // 指定响应码(返回BodyBuiler的方法称为响应体设置中间方法)
		        .ok().contentType(MediaType.APPLICATION_JSON)
		        // 响应体设置终止方法,构建数据
		        .body(userRepository.findAll(), User.class);
	}

	public Mono<ServerResponse> save(ServerRequest request) {
		// 从请求中获取要添加的数据,并将其封装为指定类型的对象,存放到Mono流中
		Mono<User> userMono = request.bodyToMono(User.class);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userRepository.saveAll(userMono),
		        User.class);
	}

	// 添加,对name的合法性进行验证
	public Mono<ServerResponse> saveValid(ServerRequest request) {
		// 从请求中获取要添加的数据,并将其封装为指定类型的对象,存放到Mono流中
		Mono<User> userMono = request.bodyToMono(User.class);

		return userMono.flatMap(t -> {
			// 对name进行合法性验证
			validateName(t.getUsername());
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userRepository.save(t), User.class);
		});
	}

	// 根据id删除: 删除成功返回200,没有找到则返回404
	public Mono<ServerResponse> delete(ServerRequest request) {
		// 从请求路径中获取id
		Long id = Long.parseLong(request.pathVariable("id"));
		return userRepository.findById(id).flatMap(t -> userRepository.delete(t).then(ServerResponse.ok().build()))
		        .switchIfEmpty(ServerResponse.notFound().build());
	}

	// 修改
	public Mono<ServerResponse> update(ServerRequest request) {
		// 从请求中获取要修改的数据,并将其封装为指定类型的对象,存放到Mono流中
		Mono<User> userMono = request.bodyToMono(User.class);
		// 从请求路径中获取id
		Long id = Long.parseLong(request.pathVariable("id"));

		return userMono.flatMap(t -> {
			// 验证姓名的合法性
			validateName(t.getUsername());
			t.setUserId(id);
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userRepository.save(t), User.class);
		});
	}

	public static void validateName(String name) {
		Stream.of(INVALIDE_NAMES).filter(invalideName -> name.equalsIgnoreCase(invalideName)).findAny()
		        .ifPresent(invalideName -> {
			        throw new RuntimeException("name" + invalideName + "使用了非法姓名");
		        });
	}
}