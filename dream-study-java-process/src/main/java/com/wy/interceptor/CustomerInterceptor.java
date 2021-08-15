package com.wy.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.wy.model.Customer;

public class CustomerInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null) {
			response.sendRedirect("login");
			return false;
		}
		return true;
	}
}