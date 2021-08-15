package com.wy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.collection.ListTool;
import com.wy.model.Exam;
import com.wy.service.ExamService;

@Service
@Transactional
public class ExamServiceImpl extends AbstractService<Exam, String> implements ExamService {

	@Override
	@Transactional(readOnly = true)
	public Exam findApplyId(String applyId) {
		List<Exam> exams = this.lambdaQuery().eq(Exam::getApplyId, applyId).orderByDesc(Exam::getUptime).list();
		if (ListTool.isNotEmpty(exams)) {
			return exams.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Exam findExamByApplyId(String applyId) {
		List<Exam> exams = this.lambdaQuery().eq(Exam::getApplyId, applyId).orderByDesc(Exam::getUptime).list();
		if (ListTool.isNotEmpty(exams)) {
			for (Exam e : exams) {
				if (!e.getAgree()) {
					return e;
				}
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Exam> findExamsByApplyId(String applyId) {
		return this.lambdaQuery().eq(Exam::getApplyId, applyId).orderByAsc(Exam::getUptime).list();
	}
}