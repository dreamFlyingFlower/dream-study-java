package com.wy.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyInterceptor implements HandlerInterceptor {

	/**
	 * 该方法将会在处理请求之前调用,在controller之前调用,若存在多个拦截器,则按配置顺序进行调用
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 有时候handler并不一定是handlermethod类型,需要进行类型检查,此处HandlerMethod可能有错误
		if (handler instanceof HandlerMethod) {
			// 获得将要调用方法执行类
			HandlerMethod method = (HandlerMethod) handler;
			System.out.println(method.getMethod().getName());
		} else {
			log.info(handler.toString());
		}
		return true;
	}

	/**
	 * 该方法是在controller执行完成,返回数据之后调用,若存在多个拦截器,则顺序正好跟配置顺序相反,写的越早,越晚调用
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {}

	/**
	 * 在进行视图渲染之后调用,做一些清理工作
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {}
}