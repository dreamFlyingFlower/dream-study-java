package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Test;

public interface TestService extends BaseService<Test, String> {

	/**
	 * 根据申请查找基础测试
	 * 
	 * @param applyId
	 * @return
	 */
	Test findTestByApplyId(String applyId);

	List<Test> findTestsByApplyId(String applyId);

	Test findApplyId(String applyId);
}