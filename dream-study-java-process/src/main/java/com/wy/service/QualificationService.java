package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Qualification;

public interface QualificationService extends BaseService<Qualification, String> {

	/**
	 * 根据申请查找入学考试资格
	 * 
	 * @param applyId
	 * @return
	 */
	Qualification findQualificationByApplyId(String applyId);

	List<Qualification> findQualificationsByApplyId(String applyId);

	Qualification findApplyId(String applyId);
}