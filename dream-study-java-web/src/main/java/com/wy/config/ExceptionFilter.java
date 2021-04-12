package com.wy.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 直接返回错误页面的统一异常处理,若是返回json数据,可使用RestControllerAdvice注解
 * 
 * 需要指定 request请求的状态码,详见{@link AbstractErrorController#getStatus}
 * 
 * 后台返回异常页面的自动配置:{@link ErrorMvcAutoConfiguration}
 * 
 * @author ParadiseWY
 * @date 2020-12-03 10:18:07
 * @git {@link https://github.com/mygodness100}
 */
// @RestControllerAdvice
@ControllerAdvice
public class ExceptionFilter {

	/**
	 * 统一异常处理,若是不返回json数据到前端,而是直接后台展示页面,可以使用该方式
	 * 
	 * spring会自动通过状态码到资源目录下的error目录找对应文件
	 * 
	 * @param e 异常
	 * @param request 请求
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public String handlerException(Exception e, HttpServletRequest request) {
		// 需要指定request的状态码
		request.setAttribute("javax.servlet.error.status_code", 404);
		// 转发到error页面
		return "forward:/error";
	}
}