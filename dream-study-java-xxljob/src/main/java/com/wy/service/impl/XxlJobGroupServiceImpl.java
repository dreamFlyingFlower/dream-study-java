package com.wy.service.impl;

import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.wy.collection.MapTool;
import com.wy.common.Constants;
import com.wy.model.XxlJobGroup;
import com.wy.model.XxlJobList;
import com.wy.result.ResultException;
import com.wy.service.XxlJobGroupService;
import com.wy.service.XxlJobService;

/**
 * 执行器实现类
 * 
 * FIXME 暂未使用
 * 
 * @author 飞花梦影
 * @date 2022-01-08 00:36:24
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class XxlJobGroupServiceImpl implements XxlJobGroupService {

	private ParameterizedTypeReference<XxlJobList<XxlJobGroup>> parameterizedTypeReference =
			new ParameterizedTypeReference<XxlJobList<XxlJobGroup>>() {
			};

	@Autowired
	private XxlJobService xxlJobService;

	@Override
	public XxlJobGroup getJobGroup(String appName) {
		XxlJobList<XxlJobGroup> result = xxlJobService.getList(Constants.XXLJOB_URL_API_GROUP_LIST,
				MapTool.builder("appname", xxlJobService.getAppName(appName)).build(), parameterizedTypeReference);
		result = Optional.ofNullable(result).orElseThrow(() -> new ResultException("appname为%s的执行器不存在", appName));
		if (result.getRecordsTotal() == 0 || CollectionUtils.isEmpty(result.getData())) {
			throw new ResultException("appname为%s的执行器不存在", appName);
		}
		return result.getData().get(0);
	}
}