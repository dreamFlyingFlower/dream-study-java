package com.wy.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.wy.model.Employee;
import com.wy.model.Qualification;
import com.wy.service.ApprovalService;
import com.wy.service.QualificationService;

@Controller
@Scope("prototype")
public class QualificationCrl {

	@Resource
	private QualificationService qualificationService;

	@Resource
	private ApprovalService approvalService;

	public String list(ModelMap map, HttpServletRequest request) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Map<Qualification, Task> rtMap = approvalService.listQualification(employee.getEid().toString());
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
	public String doApproval(ModelMap map, HttpServletRequest request, Qualification form) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Qualification qualification = qualificationService.getById(form.getQid());
		qualification.setComment(form.getComment());
		qualification.setAgree(form.getAgree());
		qualification.setScore(form.getScore());
		approvalService.doQualificationApproval(employee, qualification, form.getTaskId());
		map.put("message", "审批完成!");
		map.put("urladdress", "/itheimaoa/control/process/qualificationlist.do");
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