//package com.wy.service.impl;
//
//import java.util.List;
//
//import org.jbpm.api.model.OpenExecution;
//import org.jbpm.api.task.Assignable;
//import org.jbpm.api.task.AssignmentHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.wy.model.Employee;
//import com.wy.service.EmployeeService;
//
//@Component
//public class MyAssignmentHandler implements AssignmentHandler {
//
//	private static final long serialVersionUID = 1L;
//
//	@Autowired
//	private EmployeeService employeeService;
//
//	@Override
//	public void assign(Assignable assignable, OpenExecution execution) throws Exception {
//		List<Employee> eids = employeeService.lambdaQuery().select(Employee::getEid).list();
//		for (Employee employee : eids) {
//			assignable.addCandidateUser(employee.getEid() + "");
//		}
//	}
//}