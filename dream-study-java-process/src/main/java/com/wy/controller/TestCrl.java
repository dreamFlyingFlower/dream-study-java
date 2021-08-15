package com.wy.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.wy.model.Employee;
import com.wy.model.Test;
import com.wy.service.ApprovalService;
import com.wy.service.TestService;
import com.wy.vo.TestVo;

@Controller
@Scope("prototype")
public class TestCrl {

	@Resource
	private TestService testService;

	@Resource
	private ApprovalService approvalService;

	public String list(ModelMap map, HttpServletRequest request) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Map<Test, Task> rtMap = approvalService.listTest(employee.getEid().toString());
		map.put("rtMap", rtMap);
		return "list";
	}

	/**
	 * 审批页面
	 * 
	 * @return
	 */
	public String approval() {
		return "approval";
	}

	/**
	 * 审批
	 * 
	 * @return
	 */
	public String doApproval(ModelMap map, HttpServletRequest request, TestVo form) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Test test = testService.getById(form.getTid());
		test.setComment(form.getComment());
		test.setAgree(form.getAgree());
		test.setScore(form.getScore());
		approvalService.doTestApproval(employee, test, form.getTaskId());
		map.put("message", "审批完成!");
		map.put("urladdress", "/itheimaoa/control/process/testlist.do");
		return "message";
	}

	/**
	 * 下载
	 * 
	 * @return
	 */
	public String download() {
		return "download";
	}
}