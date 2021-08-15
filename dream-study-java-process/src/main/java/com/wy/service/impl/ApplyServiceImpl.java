package com.wy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.common.Const;
import com.wy.model.Apply;
import com.wy.model.Audition;
import com.wy.model.Customer;
import com.wy.model.Exam;
import com.wy.model.Qualification;
import com.wy.model.Resume;
import com.wy.model.Test;
import com.wy.service.ApplyService;
import com.wy.service.AuditionService;
import com.wy.service.ExamService;
import com.wy.service.QualificationService;
import com.wy.service.ResumeService;
import com.wy.service.TestService;

@Service
@Transactional
public class ApplyServiceImpl extends AbstractService<Apply, String> implements ApplyService {

	@Resource
	private ProcessEngine processEngine;

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

	/**
	 * 启动流程
	 */
	@Override
	public void startApply(Customer customer) {
		Apply apply = new Apply();
		/**
		 * 初始化用户
		 */
		apply.setCustomer(customer);
		/**
		 * 该用户选择的课程的名称
		 */
		if (customer.getIntentionCourse() != null && !"".equals(customer.getIntentionCourse())) {
			apply.setCourseName(customer.getIntentionCourse());
		}
		/**
		 * 状态提示信息
		 */
		apply.setStatus(Const.SUBMIT_RESUME);
		/**
		 * 给apply设置一个主键
		 */
		apply.setAid(UUID.randomUUID().toString());
		Map<String, Object> map = new HashMap<>();
		map.put("customerId", customer.getId());
		map.put("applyId", apply.getAid());
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("itheimaTask", map);
		/**
		 * 把piid存放在apply表中
		 */
		apply.setPi(pi.getId());
		super.save(apply);
	}

	/**
	 * 查找用户申请
	 * 
	 * @param name
	 */
	@Transactional(readOnly = true)
	public Apply findApply(String id) {
		List<Apply> applies = this.lambdaQuery().eq(Apply::getCustomerId, id).list();
		if (applies != null && applies.size() > 0) {
			for (Apply a : applies) {
				if (!a.getClose()) {
					return a;
				}
			}
		}
		return null;
	}

	@Override
	public Task findTask(String id) {
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee(id).list();
		if (tasks != null && tasks.size() > 0) {
			return tasks.get(0);
		}
		return null;
	}

	/**
	 * 提交自荐信
	 */
	@Override
	public void doSubmitResume(Customer customer, Resume resume) throws Exception {
		// 得到用户有效的申请
		Apply apply = findApply(customer.getId());
		if (apply == null) {
			throw new Exception();
		}
		// 设置自荐信属性, 保存数据库
		resume.setApply(apply);
		/**
		 * 设置自荐信的主键
		 */
		resume.setRid(UUID.randomUUID().toString());
		resume.setTitle(customer.getName() + "_自荐信");
		resumeService.save(resume);
		// 找出当前任务并结束任务
		Task task = findTask(customer.getId());
		processEngine.getTaskService().complete(task.getId());
		// processEngine.getTaskService().completeTask(task.getId());
		// 改变申请状态
		apply.setStatus(Const.RESUME_APPLY_RUNTIME);
		super.updateById(apply);
	}

	@Override
	public void doSubmitTest(Customer customer, Test test) throws Exception {
		// 得到用户有效的申请
		Apply apply = findApply(customer.getId());
		if (apply == null) {
			throw new Exception();
		}
		// 设置基础测试属性, 保存数据库
		test.setApply(apply);
		test.setTid(UUID.randomUUID().toString());
		test.setTitle(customer.getName() + "_基础测试");
		testService.save(test);
		// 找出当前任务并结束任务
		Task task = findTask(customer.getId());
		processEngine.getTaskService().complete(task.getId());
		// 改变申请状态
		apply.setStatus(Const.TEST_RUNTIME);
		super.updateById(apply);
	}

	@Override
	public void doSubmitQualification(Customer customer, Qualification qualification) throws Exception {
		// / 得到用户有效的申请
		Apply apply = findApply(customer.getId());
		if (apply == null) {
			throw new Exception();
		}
		// 设置入学考试资格属性, 保存数据库
		qualification.setApply(apply);
		qualification.setQid(UUID.randomUUID().toString());
		qualification.setTitle(customer.getName() + "_入学考试资格");
		qualificationService.save(qualification);
		// 找出当前任务并结束任务
		Task task = findTask(customer.getId());
		processEngine.getTaskService().complete(task.getId());
		// 改变申请状态
		apply.setStatus(Const.ENTRANCE_EXAM_QUALIFICATION_RUNTIME);
		super.updateById(apply);
	}

	@Override
	public void doSubmitExam(Customer customer, Exam exam) throws Exception {
		// / 得到用户有效的入学考试题
		Apply apply = findApply(customer.getId());
		if (apply == null) {
			throw new Exception();
		}
		// 设置入学考试题"属性, 保存数据库
		exam.setApply(apply);
		exam.setEid(UUID.randomUUID().toString());
		exam.setTitle(customer.getName() + "_入学考试题");
		examService.save(exam);
		// 找出当前任务并结束任务
		Task task = findTask(customer.getId());
		processEngine.getTaskService().complete(task.getId());
		// 改变申请状态
		apply.setStatus(Const.ENTRANCE_EXAM_QUALIFICATION_RUNTIME);
		super.updateById(apply);
	}

	@Override
	public void doSubmitAudition(Customer customer, Audition audition) throws Exception {
		Apply apply = findApply(customer.getId());
		if (apply == null) {
			throw new Exception();
		}
		// 设置入学考试题"属性, 保存数据库
		audition.setApply(apply);
		audition.setAid(UUID.randomUUID().toString());
		audition.setTitle(customer.getName() + "_面试申请");
		auditionService.save(audition);
		// 找出当前任务并结束任务
		Task task = findTask(customer.getId());
		processEngine.getTaskService().complete(task.getId());
		// 改变申请状态
		apply.setStatus(Const.AUDITION_RUNTIME);
		super.updateById(apply);
	}
}