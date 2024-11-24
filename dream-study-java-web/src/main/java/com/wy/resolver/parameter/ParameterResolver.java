package com.wy.resolver.parameter;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 自定义解析请求参数,根据supportsParameter()判断是否需要解析,类似{@link PathVariable}
 * 
 * @author 飞花梦影
 * @date 2024-11-23 10:43:02
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ParameterResolver implements HandlerMethodArgumentResolver {

	/**
	 * 解析参数
	 * 
	 * @param parameter 需要解析的参数,由{@link #supportsParameter(MethodParameter)}判断是否解析
	 * @param mavContainer 视图
	 * @param webRequest 请求
	 * @param binderFactory 参数绑定解析
	 * @return 解析后的对象实例
	 * @throws Exception
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		// 获得存储的session值,并指定获取的域
		webRequest.getAttribute("attributeName", RequestAttributes.SCOPE_SESSION);

		// 获得当前用户信息
		webRequest.getUserPrincipal();

		throw new MissingServletRequestPartException("找不到参数");
	}

	/**
	 * 判断是否对参数进行解析
	 * 
	 * @param parameter 参数
	 * @return true->是;false->否
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(String.class)
				&& parameter.hasParameterAnnotation(Parameters.class);
	}
}