package com.wy.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.wy.model.Employee;
import com.wy.service.EmployeeService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller("employeeAction")
@Scope("prototype")
public class EmployeeCrl {

	@Resource
	private EmployeeService employeeService;

	public String list(ModelMap map, @RequestBody Employee t) {
		map.put("pb", employeeService.getEntitys(t));
		return "list";
	}
}