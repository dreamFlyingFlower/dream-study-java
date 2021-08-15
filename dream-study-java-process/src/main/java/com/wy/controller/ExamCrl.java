package com.wy.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.wy.model.Employee;
import com.wy.model.Exam;
import com.wy.service.ApprovalService;
import com.wy.service.ExamService;
import com.wy.vo.ExamVo;

@Controller
@Scope("prototype")
public class ExamCrl {

	@Resource
	private ExamService examService;

	@Resource
	private ApprovalService approvalService;

	public String list(ModelMap map, HttpServletRequest request) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Map<Exam, Task> rtMap = approvalService.listExam(employee.getEid().toString());
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
	public String doApproval(ModelMap map, HttpServletRequest request, ExamVo form) {
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		Exam exam = examService.getById(form.getEid());
		exam.setComment(form.getComment());
		exam.setAgree(form.getAgree());
		exam.setScore(form.getScore());
		approvalService.doExamApproval(employee, exam, form.getTaskId());
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