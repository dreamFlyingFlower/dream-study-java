package com.wy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wy.model.Customer;
import com.wy.service.CustomerService;

@SpringBootTest
public class CustomerTest {

	@Autowired
	CustomerService customerService;

	@Test
	public void save() {
		Customer customer = new Customer();
		customer.setAddress("address");
		customer.setEmail("email");
		customer.setMessage("message");
		customer.setName("王五");
		customer.setQq("qq");
		customer.setGender("男");
		customer.setInfoSource("infoSource");
		customer.setCustomerStatus("customerStatus");
		customerService.save(customer);
		System.out.println(customerService);
	}
}