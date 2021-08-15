package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Exam;

public interface ExamService extends BaseService<Exam, String> {

	/**
	 * 根据申请查找入学考试题
	 * 
	 * @param applyId
	 * @return
	 */
	Exam findExamByApplyId(String applyId);

	List<Exam> findExamsByApplyId(String applyId);

	Exam findApplyId(String applyId);
}