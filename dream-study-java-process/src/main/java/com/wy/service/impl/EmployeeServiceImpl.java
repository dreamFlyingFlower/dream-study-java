package com.wy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.collection.ListTool;
import com.wy.model.Employee;
import com.wy.service.EmployeeService;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl extends AbstractService<Employee, String> implements EmployeeService {

	@Override
	public Employee checkLogin(String username, String password) {
		List<Employee> employees =
				lambdaQuery().eq(Employee::getUsername, username).eq(Employee::getPassword, password).list();
		if (ListTool.isNotEmpty(employees)) {
			return employees.get(0);
		}
		return null;
	}
}