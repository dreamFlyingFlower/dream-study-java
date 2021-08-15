package com.wy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.collection.ListTool;
import com.wy.model.Test;
import com.wy.service.TestService;

@Service
@Transactional
public class TestServiceImpl extends AbstractService<Test, String> implements TestService {

	@Override
	@Transactional(readOnly = true)
	public Test findApplyId(String applyId) {
		List<Test> tests = this.lambdaQuery().eq(Test::getApplyId, applyId).orderByDesc(Test::getUptime).list();
		if (ListTool.isNotEmpty(tests)) {
			return tests.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Test findTestByApplyId(String applyId) {
		List<Test> tests = this.lambdaQuery().eq(Test::getApplyId, applyId).orderByDesc(Test::getUptime).list();
		if (ListTool.isNotEmpty(tests)) {
			for (Test t : tests) {
				if (!t.getAgree()) {
					return t;
				}
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Test> findTestsByApplyId(String applyId) {
		return this.lambdaQuery().eq(Test::getApplyId, applyId).orderByAsc(Test::getUptime).list();
	}
}