package com.wy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wy.model.Employee;
import com.wy.service.EmployeeService;

@SpringBootTest
public class EmployeeTest {

	@Autowired
	EmployeeService employeeService;

	@Test
	public void save() {
		Employee employee = new Employee();
		employee.setDepartment(null);
		employee.setPassword("password");
		employee.setRealname("老方");
		employee.setRoles(null);
		employee.setUsername("laofang");
		employeeService.save(employee);
		System.out.println("save");
	}
}