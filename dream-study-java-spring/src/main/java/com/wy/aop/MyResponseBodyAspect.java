package com.wy.aop;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 在Spring将Controller返回结果转成JSON数据之前的操作
 *
 * @author 飞花梦影
 * @date 2022-05-21 15:37:05
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@ControllerAdvice
public class MyResponseBodyAspect implements ResponseBodyAdvice<Object> {

	/**
	 * 是否需要拦截方法返回值
	 * 
	 * @param returnType 方法返回类型
	 * @param converterType 进行转换的类型
	 * @return true->该方法会拦截,false->该方法不会拦截
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return false;
	}

	/**
	 * 当进行拦截时执行的方法
	 * 
	 * @param body 结果
	 * @param returnType 返回类型
	 * @param selectedContentType 响应类型
	 * @param selectedConverterType 需要转换到response中的类型
	 * @param request 当前请求
	 * @param response 当前响应
	 * @return 修改后的结果
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		return null;
	}
}