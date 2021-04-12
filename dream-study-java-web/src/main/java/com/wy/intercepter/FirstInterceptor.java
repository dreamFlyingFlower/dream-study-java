package com.wy.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * spring的interceptor,多个拦截器的运行是一个环形,按顺序先调用preHandle方法,之后反向调用postHandle方法
 * 
 * @author 飞花梦影
 * @date 2019-04-19 00:28:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
public class FirstInterceptor implements HandlerInterceptor {

	/**
	 * 前置方法:在进入方法之前进行拦截,拿不到参数的具体值
	 * 
	 * @param request 请求信息
	 * @param response 响应信息
	 * @param handler 方法,根据情况,通常情况下可以强转为{@link HandlerMethod}
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			// 方法类
			handlerMethod.getBean().getClass().getName();
			// 方法名
			handlerMethod.getMethod();
			// 前置方法拿不到参数的具体值,该值为null
			handlerMethod.getMethodParameters();
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	/**
	 * 后置方法:当Controller方法执行完之后,还未渲染视图之前调用,可以对视图进行操作
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 完成方法:在视图渲染完成之后调用
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}