package com.wy.resolver;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义异常视图解析器,返回本地的ModelAndView
 *
 * @author 飞花梦影
 * @date 2022-01-18 13:35:37
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class MyExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ex.printStackTrace();
		if (handler instanceof HandlerMethod) {
			// 获取方法实例
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			// 获取method对象
			Method method = handlerMethod.getMethod();
			log.error("the method {} exception", method.getName());
		}
		ModelAndView modelAndView = new ModelAndView();
		// 方式一
		try {
			// 将异常转发到BasicErrorController的/error
			response.sendError(500, "错误信息");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 方式二
		// modelAndView.addObject("message", ex.getMessage());
		// modelAndView.setViewName("error");

		return modelAndView;
	}
}