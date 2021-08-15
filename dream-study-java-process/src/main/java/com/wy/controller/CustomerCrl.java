package com.wy.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.wy.model.Customer;
import com.wy.service.CustomerService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
@Scope("prototype")
public class CustomerCrl {

	@Resource
	private CustomerService customerService;

	public String list(@RequestBody Customer customer, ModelMap map) {
		map.put("pb", customerService.getEntitys(customer));
		return "list";
	}
}