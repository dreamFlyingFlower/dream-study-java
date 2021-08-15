package com.wy.service;

import java.util.Map;

import org.activiti.engine.task.Task;

import com.wy.base.BaseService;
import com.wy.model.Apply;
import com.wy.model.Audition;
import com.wy.model.Employee;
import com.wy.model.Exam;
import com.wy.model.Qualification;
import com.wy.model.Resume;
import com.wy.model.Test;

/**
 * 处理申请
 */
public interface ApprovalService extends BaseService<Apply, String> {

	/**
	 * 自荐信
	 * 
	 * @param eid
	 * @return
	 */
	Map<Resume, Task> listResume(String eid);

	/**
	 * 入学测试题
	 * 
	 * @param eid
	 * @return
	 */
	Map<Test, Task> listTest(String eid);

	/**
	 * 入学考试资格
	 * 
	 * @param string
	 * @return
	 */
	Map<Qualification, Task> listQualification(String eid);

	/**
	 * 入学考试
	 * 
	 * @param eid
	 * @return
	 */
	Map<Exam, Task> listExam(String eid);

	/**
	 * 面试
	 * 
	 * @param eid
	 * @return
	 */
	Map<Audition, Task> listAudition(String eid);

	/**
	 * 处理自荐信
	 * 
	 * @param employee
	 * @param resume
	 */
	void doResumeApproval(Employee employee, Resume resume, String taskId);

	/**
	 * 入学测试题
	 * 
	 * @param employee
	 * @param resume
	 */
	void doTestApproval(Employee employee, Test test, String taskId);

	/**
	 * 入学考试资格
	 * 
	 * @param employee
	 * @param qualification
	 * @param taskId
	 */
	void doQualificationApproval(Employee employee, Qualification qualification, String taskId);

	/**
	 * 入学考试
	 * 
	 * @param employee
	 * @param exam
	 * @param taskId
	 */
	void doExamApproval(Employee employee, Exam exam, String taskId);

	/**
	 * 面试
	 * 
	 * @param employee
	 * @param audition
	 * @param taskId
	 */
	void doAuditionApproval(Employee employee, Audition audition, String taskId);

	/**
	 * 安排面试时间
	 * 
	 * @param employee
	 * @param audition
	 * @param taskId
	 */
	void doViewApproval(Employee employee, Audition audition, String taskId);
}