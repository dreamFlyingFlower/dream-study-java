package com.xxl.job.admin.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.core.biz.model.ReturnT;

/**
 * 执行器接口
 * 
 * @author 飞花梦影
 * @date 2022-01-07 20:16:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface JobGroupService {

	ReturnT<String> save(XxlJobGroup xxlJobGroup);

	ReturnT<String> update(XxlJobGroup xxlJobGroup);

	ReturnT<String> remove(int id);

	ReturnT<XxlJobGroup> loadById(int id);

	Map<String, Object> pageList(HttpServletRequest request, int start, int length, String appname, String title);
}