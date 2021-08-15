package com.wy.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.wy.model.Customer;
import com.wy.service.CustomerService;
import com.wy.vo.CustomerVo;

@Controller
@Scope("prototype")
public class FrontCustomerCrl {

	@Resource
	private CustomerService customerService;

	public String reg() {
		return "reg";
	}

	public String doReg(ModelMap map, HttpServletRequest request, CustomerVo form) {
		Customer customer = new Customer();
		customer.setAddress(form.getAddress());
		customer.setCellphone(form.getCellphone());
		customer.setCustomerStatus(form.getCustomerStatus());
		customer.setEmail(form.getEmail());
		customer.setGender(form.getGender());
		customer.setInfoSource(form.getInfoSource());
		customer.setIntentionCourse(form.getIntentionCourse());
		customer.setMessage(form.getMessage());
		customer.setName(form.getName());
		customer.setQq(form.getQq());
		customerService.save(customer);
		form.setMessage("注册成功,马上登录!");
		map.put("urladdress", "itheimaoa/customer/login.do");
		return "message";
	}

	public String login() {
		return "login";
	}

	public String doLogin(ModelMap map, HttpServletRequest request, CustomerVo form) {
		Customer customer = customerService.checkLogin(form.getName(), form.getCellphone());
		if (customer != null) {
			request.getSession().setAttribute("customer", customer);
			return "action2apply";
		}
		return "login";
	}
}