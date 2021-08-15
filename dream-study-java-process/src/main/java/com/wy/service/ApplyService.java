package com.wy.service;


import org.activiti.engine.task.Task;

import com.wy.base.BaseService;
import com.wy.model.Apply;
import com.wy.model.Audition;
import com.wy.model.Customer;
import com.wy.model.Exam;
import com.wy.model.Qualification;
import com.wy.model.Resume;
import com.wy.model.Test;

public interface ApplyService extends BaseService<Apply, String> {

	void startApply(Customer customer);

	Apply findApply(String id);

	/**
	 * 我当前的申请
	 * 
	 * @param id
	 * @return
	 */
	Task findTask(String id);

	/**
	 * 自荐信提交
	 * 
	 * @param customer
	 * @param resume
	 */
	void doSubmitResume(Customer customer, Resume resume) throws Exception;

	/**
	 * 基础测试提交
	 * 
	 * @param customer
	 * @param test
	 */
	void doSubmitTest(Customer customer, Test test) throws Exception;

	/**
	 * 入学考试资格
	 * 
	 * @param customer
	 * @param test
	 */
	void doSubmitQualification(Customer customer, Qualification qualification) throws Exception;

	/**
	 * 入学考试题
	 * 
	 * @param customer
	 * @param exam
	 */
	void doSubmitExam(Customer customer, Exam exam) throws Exception;

	/**
	 * 面试申请
	 * 
	 * @param customer
	 * @param audition
	 * @throws Exception
	 */
	void doSubmitAudition(Customer customer, Audition audition) throws Exception;
}