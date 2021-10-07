package com.wy.study;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.wy.collection.MapTool;

/**
 * 使用RestTemplate
 * 
 * @author 飞花梦影
 * @date 2021-10-02 10:19:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyRestTemplate {

	@Autowired
	private RestTemplate restTemplate;

	public void test() {
		// restTemplate传输普通请求,返回没有泛型的类
		restTemplate.exchange("http://ip:port/", HttpMethod.GET, null, User.class).getBody();
		// 返回指定泛型的结果
		ParameterizedTypeReference<List<User>> typeReference = new ParameterizedTypeReference<List<User>>() {
		};
		ResponseEntity<List<User>> responseEntity =
				restTemplate.exchange("http://ip:port/", HttpMethod.GET, null, typeReference);
		System.out.println(responseEntity.getBody());
		// 在请求中添加请求头
		// 定义HTTP的头信息
		HttpHeaders httpHeaders = new HttpHeaders();
		// 认证的原始信息,由用户名和密码拼接而成
		String auth = "cat:123456";
		// 进行加密处理
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		String authHeader = "Basic " + new String(encodedAuth);
		httpHeaders.set("Authorization", authHeader);
		restTemplate.exchange("http://ip:port/", HttpMethod.GET, new HttpEntity<User>(httpHeaders), User.class)
				.getBody();
		// 使用RequestEntity发送请求
		// 构建无参地址
		UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://ip:port/").build();
		// 构建直接拼接在URI后面的参数
		UriComponentsBuilder.fromUriString("http://ip:port/name={name}&password={password}").build("admin", "123456");
		UriComponentsBuilder.fromUriString("http://ip:port/")
				.build(MapTool.builder("name", "admin").put("password", "123456").build());
		RequestEntity<Void> requestEntity =
				RequestEntity.get(uriComponents.toUri()).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<User> exchange = restTemplate.exchange(requestEntity, User.class);
		System.out.println(exchange);
	}
}