package dream.study.spring.resttemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import dream.flying.flower.collection.MapHelper;
import dream.study.spring.util.RestTemplateUtil;

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
		restTemplate.exchange("http://ip:port/", HttpMethod.GET, null, typeReference);
		// 在请求中添加请求头
		HttpHeaders httpHeaders = new HttpHeaders();
		// 认证的原始信息,由用户名和密码拼接而成
		String auth = "cat:123456";
		// 进行加密处理
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		String authHeader = "Basic " + new String(encodedAuth);
		httpHeaders.set("Authorization", authHeader);
		restTemplate.exchange("http://ip:port/", HttpMethod.GET, new HttpEntity<User>(httpHeaders), User.class)
				.getBody();
		// 发送带参数的POST请求,参数只能用MultiValueMap类型,否则响应方接收不到
		MultiValueMap<String, Object> params =
				RestTemplateUtil.builder().add("username", "admin").add("password", "123456").build();
		restTemplate.exchange("http://ip:port", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, Object>>(params, httpHeaders), String.class);
		restTemplate.postForEntity("http://ip:port", new HttpEntity<MultiValueMap<String, Object>>(params, httpHeaders),
				String.class);
		// 如果POST不需要请求头参数,可直接使用postForObject,直接传递参数
		restTemplate.postForObject("http://ip:port", params, String.class);
		// 发送带cookie的请求
		HttpHeaders httpHeadersCookie = new HttpHeaders();
		httpHeadersCookie.addAll(HttpHeaders.COOKIE,
				new ArrayList<>(Arrays.asList("cookieName" + "=" + "cookieValue")));
		restTemplate.exchange("http://ip:port", HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, Object>>(params, httpHeadersCookie), String.class);
		// 使用Get发送请求,参数可以封装到Map中,只能用HashMap,且URL要使用占位符
		Map<String, Object> paramMap = MapHelper.builder("pageIndex", "1").put("pageSize", "10").build();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authHeader);
		HttpEntity<MultiValueMap<String, Object>> httpEntity =
				new HttpEntity<MultiValueMap<String, Object>>(null, headers);
		restTemplate.exchange("http://127.0.0.1:8080/getList?pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET,
				httpEntity, String.class, paramMap);
		// 发送DELETE请求,基本同GET
		restTemplate.exchange("http://127.0.0.1:8080/delete/{id}", HttpMethod.DELETE, null, String.class,
				MapHelper.builder("id", 1).build());

		// 使用RequestEntity发送请求
		// 构建无参地址
		UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://ip:port/").build();
		// 构建直接拼接在URI后面的参数
		UriComponentsBuilder.fromUriString("http://ip:port/name={name}&password={password}").build("admin", "123456");
		UriComponentsBuilder.fromUriString("http://ip:port/")
				.build(MapHelper.builder("name", "admin").put("password", "123456").build());
		RequestEntity<Void> requestEntity =
				RequestEntity.get(uriComponents.toUri()).accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<User> exchange = restTemplate.exchange(requestEntity, User.class);
		System.out.println(exchange);
	}
}