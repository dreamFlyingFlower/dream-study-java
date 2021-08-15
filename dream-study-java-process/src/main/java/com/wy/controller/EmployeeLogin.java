package com.wy.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wy.model.Employee;
import com.wy.service.EmployeeService;

@Controller("employeeLogin")
@Scope("prototype")
public class EmployeeLogin {

	@Resource
	private EmployeeService employeeService;

	public String login() {
		return "login";
	}

	public String doLogin(String username, String password, HttpServletRequest request) {
		Employee employee = employeeService.checkLogin(username, password);
		if (employee != null) {
			request.getSession().setAttribute("employee", employee);
			return "index";
		}
		return "login";
	}
}