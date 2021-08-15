package com.wy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.collection.ListTool;
import com.wy.model.Customer;
import com.wy.service.CustomerService;

@Service("customerService")
@Transactional
public class CustomerServiceImpl extends AbstractService<Customer, String> implements CustomerService {

	@Override
	public Customer checkLogin(String name, String cellphone) {
		List<Customer> customers =
				lambdaQuery().eq(Customer::getName, name).eq(Customer::getCellphone, cellphone).list();
		if (ListTool.isNotEmpty(customers)) {
			return customers.get(0);
		}
		return null;
	}
}