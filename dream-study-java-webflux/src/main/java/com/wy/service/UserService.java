package com.wy.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.model.User;

import reactor.core.publisher.Flux;

/**
 * 测试WebFlux的用户业务实现类
 * 
 * @author ParadiseWY
 * @date 2020-11-23 14:15:57
 * @git {@link https://github.com/mygodness100}
 */
@Service
public class UserService {

	public Flux<User> testFlux() {
		List<User> users = new ArrayList<User>();
		users.add(User.builder().userId(2l).username("test1").build());
		users.add(User.builder().userId(3l).username("test2").build());
		// 将数组转换为Flux
		Flux.fromArray(new Integer[] { 2, 3 });
		// 将流转换为Flux
		Flux.fromStream(users.stream());
		// 将迭代转换为Flux
		return Flux.fromIterable(users);
	}
}