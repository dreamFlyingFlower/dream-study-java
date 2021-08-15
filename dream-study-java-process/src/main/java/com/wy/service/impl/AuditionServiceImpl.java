package com.wy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.collection.ListTool;
import com.wy.model.Audition;
import com.wy.service.AuditionService;

@Service
@Transactional
public class AuditionServiceImpl extends AbstractService<Audition, String> implements AuditionService {

	@Override
	@Transactional(readOnly = true)
	public Audition findApplyId(String applyId) {
		List<Audition> auditions =
				this.lambdaQuery().eq(Audition::getApplyId, applyId).orderByDesc(Audition::getUptime).list();
		if (ListTool.isNotEmpty(auditions)) {
			return auditions.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Audition findAuditionByApplyId(String applyId) {
		List<Audition> auditions =
				this.lambdaQuery().eq(Audition::getApplyId, applyId).orderByDesc(Audition::getUptime).list();
		if (ListTool.isNotEmpty(auditions)) {
			for (Audition a : auditions) {
				if (!a.getAgree()) {
					return a;
				}
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Audition> findAuditionsByApplyId(String applyId) {
		return this.lambdaQuery().eq(Audition::getApplyId, applyId).orderByAsc(Audition::getUptime).list();
	}
}