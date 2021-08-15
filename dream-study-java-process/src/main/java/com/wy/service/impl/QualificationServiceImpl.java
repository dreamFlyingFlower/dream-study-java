package com.wy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.collection.ListTool;
import com.wy.model.Qualification;
import com.wy.service.QualificationService;

@Service
@Transactional
public class QualificationServiceImpl extends AbstractService<Qualification, String> implements QualificationService {

	@Override
	@Transactional(readOnly = true)
	public Qualification findApplyId(String applyId) {
		List<Qualification> results =
				this.lambdaQuery().eq(Qualification::getApplyId, applyId).orderByDesc(Qualification::getUptime).list();
		if (ListTool.isNotEmpty(results)) {
			return results.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Qualification findQualificationByApplyId(String applyId) {
		List<Qualification> results =
				this.lambdaQuery().eq(Qualification::getApplyId, applyId).orderByDesc(Qualification::getUptime).list();
		if (ListTool.isNotEmpty(results)) {
			for (Qualification q : results) {
				if (!q.getAgree()) {
					return q;
				}
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Qualification> findQualificationsByApplyId(String applyId) {
		return this.lambdaQuery().eq(Qualification::getApplyId, applyId).orderByAsc(Qualification::getUptime).list();
	}
}