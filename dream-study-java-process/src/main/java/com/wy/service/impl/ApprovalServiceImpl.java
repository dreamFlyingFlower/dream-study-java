package com.wy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.common.Const;
import com.wy.model.Apply;
import com.wy.model.Audition;
import com.wy.model.Employee;
import com.wy.model.Exam;
import com.wy.model.Qualification;
import com.wy.model.Resume;
import com.wy.model.Test;
import com.wy.service.ApplyService;
import com.wy.service.ApprovalService;
import com.wy.service.AuditionService;
import com.wy.service.ExamService;
import com.wy.service.QualificationService;
import com.wy.service.ResumeService;
import com.wy.service.TestService;

@Service
@Transactional
public class ApprovalServiceImpl extends AbstractService<Apply, String> implements ApprovalService {

	@Autowired
	private ProcessEngine processEngine;

	@Autowired
	private ApplyService applyService;

	@Autowired
	private ResumeService resumeService;

	@Autowired
	private TestService testService;

	@Autowired
	private QualificationService qualificationService;

	@Autowired
	private ExamService examService;

	@Autowired
	private AuditionService auditionService;

	/**
	 * 处理自荐信
	 */
	@Override
	public Map<Resume, Task> listResume(String eid) {
		Map<Resume, Task> rtMap = new HashMap<Resume, Task>();
		// 把当前登陆人能够审批的所有的自荐信审批的任务全部查询出来了
		List<Task> tasks =
				processEngine.getTaskService().createTaskQuery().taskCandidateUser(eid).taskName("自荐信审批").list();
		for (Task t : tasks) {
			String applyId = (String) processEngine.getTaskService().getVariable(t.getId(), "applyId");
			Apply apply = applyService.getById(applyId);
			// apply.getResumes();
			// 这块有问题
			// 根据applyid查询所有的自荐信的审批
			List<Resume> resumes = resumeService.findResumesByApplyId(apply.getAid());
			for (Resume r : resumes) {
				if (r.getEmployee() == null) {
					rtMap.put(r, t);
				}
			}
		}
		return rtMap;
	}

	/**
	 * 处理基础测试题
	 */
	@Override
	public Map<Test, Task> listTest(String eid) {
		Map<Test, Task> rtMap = new HashMap<Test, Task>();
		List<Task> tasks =
				processEngine.getTaskService().createTaskQuery().taskCandidateUser(eid).taskName("基础测试审批").list();
		for (Task t : tasks) {
			String applyId = (String) processEngine.getTaskService().getVariable(t.getId(), "applyId");
			Apply apply = applyService.getById(applyId);
			// FIXME 这块有问题
			List<Test> tests = testService.findTestsByApplyId(apply.getAid());
			for (Test test : tests) {
				if (test.getEmployee() == null) {
					rtMap.put(test, t);
				}
			}
		}
		return rtMap;
	}

	@Override
	public Map<Qualification, Task> listQualification(String eid) {
		Map<Qualification, Task> rtMap = new HashMap<Qualification, Task>();
		List<Task> tasks =
				processEngine.getTaskService().createTaskQuery().taskCandidateUser(eid).taskName("入学考试资格审批").list();
		for (Task t : tasks) {
			String applyId = (String) processEngine.getTaskService().getVariable(t.getId(), "applyId");
			Apply apply = applyService.getById(applyId);
			// FIXME 这块有问题
			List<Qualification> qualifications = qualificationService.findQualificationsByApplyId(apply.getAid());
			for (Qualification q : qualifications) {
				if (q.getEmployee() == null) {
					rtMap.put(q, t);
				}
			}
		}
		return rtMap;
	}

	@Override
	public Map<Exam, Task> listExam(String eid) {
		Map<Exam, Task> rtMap = new HashMap<Exam, Task>();
		List<Task> tasks =
				processEngine.getTaskService().createTaskQuery().taskCandidateUser(eid).taskName("入学考试审批").list();
		for (Task t : tasks) {
			String applyId = (String) processEngine.getTaskService().getVariable(t.getId(), "applyId");
			Apply apply = applyService.getById(applyId);
			// FIXME 这块有问题
			List<Exam> exams = examService.findExamsByApplyId(apply.getAid());
			for (Exam e : exams) {
				if (e.getEmployee() == null) {
					rtMap.put(e, t);
				}
			}
		}
		return rtMap;
	}

	@Override
	public Map<Audition, Task> listAudition(String eid) {
		Map<Audition, Task> rtMap = new HashMap<Audition, Task>();
		List<Task> tasks =
				processEngine.getTaskService().createTaskQuery().taskCandidateUser(eid).taskName("综合素质审核").list();
		for (Task t : tasks) {
			String applyId = (String) processEngine.getTaskService().getVariable(t.getId(), "applyId");
			Apply apply = applyService.getById(applyId);
			// FIXME 这块有问题
			List<Audition> auditions = auditionService.findAuditionsByApplyId(apply.getAid());
			for (Audition a : auditions) {
				if (a.getEmployee() == null) {
					rtMap.put(a, t);
				}
			}
		}
		return rtMap;
	}

	/**
	 * 处理自荐信审批
	 */
	@Override
	public void doResumeApproval(Employee employee, Resume resume, String taskId) {
		resume.setEmployee(employee);
		resume.setApproveTime(new Date());
		// 结束自荐信审批任务
		if (resume.getAgree()) {
			resume.setStatus(Const.RESUME_APPLY_PASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().complete(taskId, "to 提交基础测试");
		} else {
			resume.setStatus(Const.RESUME_APPLY_NOPASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().complete(taskId, "to 提交自荐信");
		}
		// 保存自荐信
		resumeService.updateById(resume);
		Apply apply = resume.getApply();
		if (resume.getAgree()) {
			apply.setStatus(Const.SUBMIT_TEST);
		} else {
			apply.setStatus(Const.RESUME_APPLY_NOPASS);
		}
		apply.setScore(resume.getScore());
		applyService.updateById(apply);
	}

	/**
	 * 处理基础测试
	 */
	@Override
	public void doTestApproval(Employee employee, Test test, String taskId) {
		test.setEmployee(employee);
		test.setApproveTime(new Date());
		// 结束基础测试任务
		if (test.getAgree()) {
			test.setStatus(Const.TEST_PASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().completeTask(taskId, "to 入学考试资格申请");
		} else {
			test.setStatus(Const.TEST_NOPASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().completeTask(taskId, "to 提交基础测试");
		}
		// 保存自荐信
		testService.updateById(test);
		Apply apply = test.getApply();
		if (test.getAgree()) {
			apply.setStatus(Const.SUBMIT_QUALIFICATION);
		} else {
			apply.setStatus(Const.TEST_NOPASS);
		}
		apply.setScore(apply.getScore() + test.getScore());
		applyService.updateById(apply);
	}

	@Override
	public void doQualificationApproval(Employee employee, Qualification qualification, String taskId) {
		qualification.setEmployee(employee);
		qualification.setApproveTime(new Date());
		// 结束入学考试资格
		if (qualification.getAgree()) {
			qualification.setStatus(Const.ENTRANCE_EXAM_QUALIFICATION_PASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().completeTask(taskId, "to 提交入学考试题");
		} else {
			qualification.setStatus(Const.ENTRANCE_EXAM_NOPASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().completeTask(taskId, "to 入学考试资格申请");
		}
		// 保存入学考试资格
		qualificationService.updateById(qualification);
		Apply apply = qualification.getApply();
		if (qualification.getAgree()) {
			apply.setStatus(Const.SUBMIT_EXAM);
		} else {
			apply.setStatus(Const.ENTRANCE_EXAM_QUALIFICATION_NOPASS);
		}
		apply.setScore(apply.getScore() + qualification.getScore());
		applyService.updateById(apply);
	}

	@Override
	public void doExamApproval(Employee employee, Exam exam, String taskId) {
		exam.setEmployee(employee);
		exam.setApproveTime(new Date());
		// 结束入学考试
		if (exam.getAgree()) {
			exam.setStatus(Const.ENTRANCE_EXAM_PASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().completeTask(taskId, "to 申请面试");
		} else {
			exam.setStatus(Const.ENTRANCE_EXAM_NOPASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().completeTask(taskId, "to 提交入学考试题");
		}
		// 保存入学考试
		examService.updateById(exam);
		Apply apply = exam.getApply();
		if (exam.getAgree()) {
			apply.setStatus(Const.SUBMIT_AUDITION);
		} else {
			apply.setStatus(Const.ENTRANCE_EXAM_QUALIFICATION_NOPASS);
		}
		apply.setScore(apply.getScore() + exam.getScore());
		applyService.updateById(apply);
	}

	@Override
	public void doAuditionApproval(Employee employee, Audition audition, String taskId) {
		audition.setEmployee(employee);
		audition.setApproveTime(new Date());
		// 结束面试
		if (audition.getAgree()) {
			audition.setStatus(Const.AUDITION_PASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().completeTask(taskId, "to 申请分配入学名额");
		} else {
			audition.setStatus(Const.AUDITION_NOPASS);
			processEngine.getTaskService().complete(taskId);
			// processEngine.getTaskService().completeTask(taskId, "to 申请面试");
		}
		// 保存面试
		auditionService.updateById(audition);
		Apply apply = audition.getApply();
		if (audition.getAgree()) {
			apply.setStatus(Const.SUBMIT_ENTRANCE);
		} else {
			apply.setStatus(Const.AUDITION_NOPASS);
		}
		apply.setScore(apply.getScore() + audition.getScore());
		applyService.updateById(apply);
	}

	@Override
	public void doViewApproval(Employee employee, Audition audition, String taskId) {
		audition.setApproveTime(new Date());
		// 结束面试
		// 保存面试
		auditionService.updateById(audition);
		Apply apply = audition.getApply();
		apply.setStatus(Const.AUDITION_PREPARE);
		applyService.updateById(apply);
	}
}