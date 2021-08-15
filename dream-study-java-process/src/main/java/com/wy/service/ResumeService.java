package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Resume;

public interface ResumeService extends BaseService<Resume, String> {

	/**
	 * 根据申请查找自荐信
	 * 
	 * @param applyId
	 * @return
	 */
	Resume findResumeByApplyId(String applyId);

	List<Resume> findResumesByApplyId(String applyId);

	Resume findApplyId(String applyId);
}