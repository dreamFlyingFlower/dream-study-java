package com.wy.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.wy.model.Employee;

public class EmployeeInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		if (employee == null) {
			response.sendRedirect("login");
			return false;
		}
		return true;
	}
}