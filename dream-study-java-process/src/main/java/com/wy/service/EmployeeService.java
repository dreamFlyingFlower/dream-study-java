package com.wy.service;

import com.wy.base.BaseService;
import com.wy.model.Employee;

public interface EmployeeService extends BaseService<Employee, String> {

	/**
	 * 员工登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	Employee checkLogin(String username, String password);
}