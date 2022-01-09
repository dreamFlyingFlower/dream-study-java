package com.wy.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.wy.common.Constants;
import com.wy.lang.StrTool;
import com.wy.model.XxlJobList;
import com.wy.properties.XxlJobProperties;
import com.wy.result.ResultException;
import com.wy.service.XxlJobService;
import com.wy.util.RestTemplateUtil;
import com.xxl.job.core.biz.model.ReturnT;

import lombok.extern.slf4j.Slf4j;

/**
 * xxljob通用方法实现
 * 
 * @author 飞花梦影
 * @date 2022-01-08 01:01:22
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@Slf4j
public class XxlJobServiceImpl implements XxlJobService {

	private static final Map<String, String> USER_COOKIE = new HashMap<>();

	private ParameterizedTypeReference<ReturnT<String>> retType = new ParameterizedTypeReference<ReturnT<String>>() {
	};

	@Value("${spring.application.name}")
	private String applicationName;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private XxlJobProperties xxlJobProperties;

	/**
	 * 构建带cookie的请求头
	 * 
	 * @return 请求头
	 */
	private HttpHeaders generateHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
//		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		httpHeaders.add(HttpHeaders.COOKIE, Constants.XXLJOB_COOKIE_LOGIN_KEY + "=" + getCookie());
		return httpHeaders;
	}

	/**
	 * 从缓存中获取cookie,缓存中没有则调用登录接口获取
	 * 
	 * @return cookie值
	 */
	private String getCookie() {
		String cookie = USER_COOKIE.get(xxlJobProperties.getUsername());
		if (StrTool.isBlank(cookie)) {
			cookie = login();
			if (StrTool.isBlank(cookie)) {
				throw new ResultException("登录xxljob-admin失败,请联系管理员");
			}
		}
		return cookie;
	}

	/**
	 * 从登录的请求接口中获得cookie
	 * 
	 * @param responseEntity 登录响应结果
	 * @return cookie值
	 */
	private String getCookie(ResponseEntity<ReturnT<String>> responseEntity) {
		// Set-Cookie=[XXL_JOB_LOGIN_IDENTITY=cookie; Path=/; HttpOnly]
		String cookie = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
		int start = cookie.indexOf(Constants.XXLJOB_COOKIE_LOGIN_KEY) + 1 + Constants.XXLJOB_COOKIE_LOGIN_KEY.length();
		cookie = cookie.substring(start, cookie.indexOf(";", start));
		USER_COOKIE.put(xxlJobProperties.getUsername(), cookie);
		return cookie;
	}

	/**
	 * 处理结果
	 * 
	 * @param responseEntity 响应
	 * @return 结果字符串
	 */
	private String handlerResponse(ResponseEntity<ReturnT<String>> responseEntity) {
		log.info("@@@:XxlJob请求结果:{}", JSON.toJSONString(responseEntity));
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			if (200 == responseEntity.getBody().getCode()) {
				return responseEntity.getBody().getContent();
			} else {
				throw new ResultException(responseEntity.getBody().getMsg());
			}
		} else {
			log.error("###:xxljob请求响应失败,错误码为:{}", responseEntity.getStatusCodeValue());
			throw new ResultException("###:xxljob请求响应失败,错误码为:%s", responseEntity.getStatusCodeValue());
		}
	}

	/**
	 * 处理结果
	 * 
	 * @param responseEntity 响应
	 * @return 结果字符串
	 */
	private <T> XxlJobList<T> handlerResponseList(ResponseEntity<XxlJobList<T>> responseEntity) {
		log.info("@@@:XxlJob请求结果:{}", JSON.toJSONString(responseEntity));
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			return responseEntity.getBody();
		} else {
			log.error("###:xxljob请求响应失败,错误码为:{}", responseEntity.getStatusCodeValue());
			throw new ResultException("###:xxljob请求响应失败,错误码为:%s", responseEntity.getStatusCodeValue());
		}
	}

	/**
	 * 获得定时任务执行器标识
	 * 
	 * 若指定了执行器标识,直接返回;若未指定,使用appName->spring.application.name->default-group
	 * 
	 * @param appName 指定的执行器标识
	 * @return 执行器标识
	 */
	@Override
	public String getAppName(String appName) {
		appName = StrTool.isBlank(appName) ? null : appName;
		return Optional.ofNullable(appName).orElseGet(() -> {
			return StrTool.isBlank(xxlJobProperties.getExecutor().getAppname())
					? StrTool.isBlank(applicationName) ? Constants.XXLJOB_DEFAULT_GROUP_NAME : applicationName
					: xxlJobProperties.getExecutor().getAppname();
		});
	}

	@Override
	public <T> XxlJobList<T> getList(String api, Map<String, Object> params,
			ParameterizedTypeReference<XxlJobList<T>> parameterizedTypeReference) {
		String url = RestTemplateUtil.generateGetUrl(xxlJobProperties.getAdminAddresses().split(",")[0] + api, params);
		ResponseEntity<XxlJobList<T>> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
				new HttpEntity<String>(generateHeader()), parameterizedTypeReference, params);
		return handlerResponseList(responseEntity);
	}

	/**
	 * Get请求
	 * 
	 * @param api 接口地址
	 * @param params 参数
	 * @return 结果
	 */
	@Override
	public String getString(String api, Map<String, Object> params) {
		String url = RestTemplateUtil.generateGetUrl(xxlJobProperties.getAdminAddresses().split(",")[0] + api, params);
		ResponseEntity<ReturnT<String>> responseEntity =
				restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(generateHeader()), retType, params);
		return handlerResponse(responseEntity);
	}

	/**
	 * http登录admin,获得cookie,由于地址可能有多个,最好是配置中的adminAddresses只写一个
	 */
	@Override
	public String login() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		RequestEntity<MultiValueMap<String, Object>> requestEntity = new RequestEntity<>(
				RestTemplateUtil.builder().add("userName", xxlJobProperties.getUsername())
						.add("password", xxlJobProperties.getPassword()).build(),
				httpHeaders, HttpMethod.POST,
				URI.create(xxlJobProperties.getAdminAddresses().split(",")[0] + Constants.XXLJOB_URL_API_LOGIN));
		ParameterizedTypeReference<ReturnT<String>> retType = new ParameterizedTypeReference<ReturnT<String>>() {
		};
		ResponseEntity<ReturnT<String>> responseEntity = restTemplate.exchange(requestEntity, retType);
		return getCookie(responseEntity);
	}

	/**
	 * Post请求传输对象
	 * 
	 * @param <T>
	 * @param api 接口地址
	 * @param t 参数
	 * @return 结果
	 */
	@Override
	public <T> String postObject(String api, T t) {
		ResponseEntity<ReturnT<String>> responseEntity =
				restTemplate.exchange(xxlJobProperties.getAdminAddresses().split(",")[0] + api, HttpMethod.POST,
						new HttpEntity<MultiValueMap<String, Object>>(RestTemplateUtil.toLinkedMultiValueMap(t),
								generateHeader()),
						retType);
		return handlerResponse(responseEntity);
	}
}