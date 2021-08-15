package com.wy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.collection.ListTool;
import com.wy.model.Resume;
import com.wy.service.ResumeService;

@Service
@Transactional
public class ResumeServiceImpl extends AbstractService<Resume, String> implements ResumeService {

	@Override
	@Transactional(readOnly = true)
	public Resume findApplyId(String applyId) {
		List<Resume> resumes = this.lambdaQuery().eq(Resume::getApplyId, applyId).orderByDesc(Resume::getUptime).list();
		if (ListTool.isNotEmpty(resumes)) {
			return resumes.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Resume findResumeByApplyId(String applyId) {
		List<Resume> resumes = this.lambdaQuery().eq(Resume::getApplyId, applyId).orderByAsc(Resume::getUptime).list();
		if (resumes != null && resumes.size() > 0) {
			for (Resume r : resumes) {
				if (!r.getAgree()) {
					return r;
				}
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Resume> findResumesByApplyId(String applyId) {
		return this.lambdaQuery().eq(Resume::getApplyId, applyId).orderByAsc(Resume::getUptime).list();
	}
}