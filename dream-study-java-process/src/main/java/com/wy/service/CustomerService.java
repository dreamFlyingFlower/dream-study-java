package com.wy.service;

import com.wy.base.BaseService;
import com.wy.model.Customer;

public interface CustomerService extends BaseService<Customer, String> {

	/**
	 * 客户登录
	 * 
	 * @param name
	 * @param cellphone
	 * @return
	 */
	Customer checkLogin(String name, String cellphone);
}