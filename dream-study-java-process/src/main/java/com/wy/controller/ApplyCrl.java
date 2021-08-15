package com.wy.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.wy.model.Apply;
import com.wy.model.Audition;
import com.wy.model.Customer;
import com.wy.model.Exam;
import com.wy.model.Qualification;
import com.wy.model.Resume;
import com.wy.model.Test;
import com.wy.service.ApplyService;
import com.wy.service.AuditionService;
import com.wy.service.CustomerService;
import com.wy.service.ExamService;
import com.wy.service.QualificationService;
import com.wy.service.ResumeService;
import com.wy.service.TestService;
import com.wy.util.UploadUtils;
import com.wy.vo.ApplyVo;

@Controller
@Scope("prototype")
public class ApplyCrl {

	@Resource
	private CustomerService customerService;

	@Resource
	private ApplyService applyService;

	@Resource
	private ResumeService resumeService;

	@Resource
	private TestService testService;

	@Resource
	private QualificationService qualificationService;

	@Resource
	private ExamService examService;

	@Resource
	private AuditionService auditionService;

	public String applyIndex() {
		return "applyIndex";
	}

	public String apply(ModelMap map, HttpServletRequest request) {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer != null) {
			Apply apply = applyService.findApply(customer.getId());
			if (apply != null) {
				return "action2apply";
			}
		}
		return "apply";
	}

	/**
	 * 启动流程
	 * 
	 * @return
	 */
	public String doApply(ModelMap map, HttpServletRequest request, ApplyVo form) {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer != null) {
			customer.setIntentionCourse(form.getIntentionCourse());
			applyService.startApply(customer);
		}
		customerService.updateById(customer);
		return "action2apply";
	}

	/**
	 * 查找个人申请
	 * 
	 * @return
	 */
	public String findMyApply(ModelMap map, HttpServletRequest request) {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		Apply apply = applyService.findApply(customer.getId());
		// Resume resume = resumeService.findApplyId(apply.getAid());
		List<Resume> resumes = resumeService.findResumesByApplyId(apply.getAid());
		// Test test = testService.findApplyId(apply.getAid());
		List<Test> tests = testService.findTestsByApplyId(apply.getAid());
		List<Qualification> qualifications = qualificationService.findQualificationsByApplyId(apply.getAid());
		List<Exam> exams = examService.findExamsByApplyId(apply.getAid());
		List<Audition> auditions = auditionService.findAuditionsByApplyId(apply.getAid());
		map.put("auditions", auditions);
		map.put("exams", exams);
		map.put("qualifications", qualifications);
		map.put("resumes", resumes);
		map.put("tests", tests);
		map.put("apply", apply);
		return "findMyApply";
	}

	/**
	 * 查看用户当前任务
	 * 
	 * @return
	 */
	public String findMyTask(ModelMap map, HttpServletRequest request, ApplyVo form) {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		Task task = applyService.findTask(customer.getId());
		if (task == null) {
			form.setMessage("您当前没有任务!");
			return "back";
		}
		String taskName = task.getName();
		if ("提交自荐信".equals(taskName)) {
			return "submitResume";
		} else if ("提交基础测试".equals(taskName)) {
			return "submitTest";
		} else if ("入学考试资格申请".equals(taskName)) {
			return "submitQualification";
		} else if ("提交入学考试题".equals(taskName)) {
			return "submitExam";
		} else if ("申请面试".equals(taskName)) {
			return "submitAudition";
		}
		form.setMessage("您当前任务:" + taskName);
		return "back";
	}

	/**
	 * 提交自荐信
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doSubmitResume(ModelMap map, HttpServletRequest request, ApplyVo form) throws Exception {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		Resume resume = new Resume();
		String ext = UploadUtils.getExt(form.getFileFileName());
		String filePath = UploadUtils.saveUploadFile(form.getFile(), ext, request);
		resume.setResumePath(filePath);
		resume.setMessage(form.getMessage());
		applyService.doSubmitResume(customer, resume);
		form.setMessage("自荐信提交成功!");
		map.put("urladdress", "/itheimaoa/customer/process/findMyApply.do");
		return "message";
	}

	/**
	 * 提交基础测试
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doSubmitTest(ModelMap map, HttpServletRequest request, ApplyVo form) throws Exception {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		Test test = new Test();
		String ext = UploadUtils.getExt(form.getFileFileName());
		String filePath = UploadUtils.saveUploadFile(form.getFile(), ext, request);
		test.setResumePath(filePath);
		test.setMessage(form.getMessage());
		applyService.doSubmitTest(customer, test);
		form.setMessage("基础测试题提交成功!");
		map.put("urladdress", "/itheimaoa/customer/process/findMyApply.do");
		return "message";
	}

	/**
	 * 提交入学考试资格
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doSubmitQualification(ModelMap map, HttpServletRequest request, ApplyVo form) throws Exception {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		Qualification qualification = new Qualification();
		qualification.setMessage(form.getMessage());
		applyService.doSubmitQualification(customer, qualification);
		form.setMessage("入学考试资格提交成功!");
		map.put("urladdress", "/itheimaoa/customer/process/findMyApply.do");
		return "message";
	}

	/**
	 * 提交入学考试
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doSubmitExam(ModelMap map, HttpServletRequest request, ApplyVo form) throws Exception {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		Exam exam = new Exam();
		exam.setMessage(form.getMessage());
		applyService.doSubmitExam(customer, exam);
		form.setMessage("入学考试题提交成功!");
		map.put("urladdress", "/itheimaoa/customer/process/findMyApply.do");
		return "message";
	}

	/**
	 * 提交面试
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doSubmitAudition(ModelMap map, HttpServletRequest request, ApplyVo form) throws Exception {
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		Audition audition = new Audition();
		audition.setMessage(form.getMessage());
		applyService.doSubmitAudition(customer, audition);
		form.setMessage("面试申请提交成功!");
		map.put("urladdress", "/itheimaoa/customer/process/findMyApply.do");
		return "message";
	}
}