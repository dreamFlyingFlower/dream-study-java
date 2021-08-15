package com.wy.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.wy.model.Employee;
import com.wy.model.Resume;
import com.wy.service.ApprovalService;
import com.wy.service.ResumeService;
import com.wy.vo.ResumeVo;

@Controller
@Scope("prototype")
public class ResumeCrl {

	@Resource
	private ResumeService resumeService;

	@Resource
	private ApprovalService approvalService;

	public String list(ModelMap map, HttpServletRequest request) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Map<Resume, Task> rtMap = approvalService.listResume(employee.getEid().toString());
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
	public String doApproval(ModelMap map, HttpServletRequest request, ResumeVo form) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Resume resume = resumeService.getById(form.getRid());
		resume.setComment(form.getComment());
		resume.setAgree(form.getAgree());
		resume.setScore(form.getScore());
		approvalService.doResumeApproval(employee, resume, form.getTaskId());
		map.put("message", "审批完成!");
		map.put("urladdress", "/itheimaoa/control/process/resumelist.do");
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