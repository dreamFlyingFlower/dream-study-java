package com.wy.service;

import java.util.List;

import com.wy.base.BaseService;
import com.wy.model.Audition;

public interface AuditionService extends BaseService<Audition, String> {

	/**
	 * 根据申请查找面试申请
	 * 
	 * @param applyId
	 * @return
	 */
	Audition findAuditionByApplyId(String applyId);

	List<Audition> findAuditionsByApplyId(String applyId);

	Audition findApplyId(String applyId);
}