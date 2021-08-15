package com.wy.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.wy.model.Audition;
import com.wy.model.Employee;
import com.wy.service.ApprovalService;
import com.wy.service.AuditionService;
import com.wy.vo.AuditionVo;

@Controller
@Scope("prototype")
public class AuditionCrl {

	@Resource
	private AuditionService auditionService;

	@Resource
	private ApprovalService approvalService;

	public String list(ModelMap map, HttpServletRequest request) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Map<Audition, Task> rtMap = approvalService.listAudition(employee.getEid().toString());
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
	public String doApproval(ModelMap map, HttpServletRequest request, AuditionVo form) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Audition audition = auditionService.getById(form.getAid());
		audition.setComment(form.getComment());
		audition.setAgree(form.getAgree());
		audition.setScore(form.getScore());
		approvalService.doAuditionApproval(employee, audition, form.getTaskId());
		map.put("message", "审批完成!");
		map.put("urladdress", "/itheimaoa/control/process/auditionlist.do");
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