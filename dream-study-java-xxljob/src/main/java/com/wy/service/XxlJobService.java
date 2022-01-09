package com.wy.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;

import com.wy.model.XxlJobList;

/**
 * xxljob通用方法
 * 
 * @author 飞花梦影
 * @date 2022-01-08 01:00:39
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface XxlJobService {

	/**
	 * Get请求
	 * 
	 * @param api 接口地址
	 * @param params 参数
	 * @return 结果
	 */
	String getString(String api, Map<String, Object> params);

	/**
	 * Get请求
	 * 
	 * @param api 接口地址
	 * @param params 参数
	 * @param parameterizedTypeReference 结果类型
	 * @return 结果
	 */
	<T> XxlJobList<T> getList(String api, Map<String, Object> params,
			ParameterizedTypeReference<XxlJobList<T>> parameterizedTypeReference);

	/**
	 * 获得定时任务执行器标识
	 * 
	 * 若指定了执行器标识,直接返回;若未指定,使用appName->spring.application.name->default-group
	 * 
	 * @param appName 指定的执行器标识
	 * @return 执行器标识
	 */
	String getAppName(String appName);

	/**
	 * 内置登录
	 * 
	 * @return cookie
	 */
	String login();

	/**
	 * Post请求传输对象
	 * 
	 * @param <T>
	 * @param api 接口地址
	 * @param t 参数
	 * @return 结果
	 */
	<T> String postObject(String api, T t);
}