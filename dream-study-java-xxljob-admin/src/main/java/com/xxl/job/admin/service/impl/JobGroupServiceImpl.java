package com.xxl.job.admin.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobRegistry;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.dao.XxlJobInfoDao;
import com.xxl.job.admin.dao.XxlJobRegistryDao;
import com.xxl.job.admin.service.JobGroupService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.RegistryConfig;

/**
 * 执行器实现类
 * 
 * @author 飞花梦影
 * @date 2022-01-07 20:17:50
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class JobGroupServiceImpl implements JobGroupService {

	@Autowired
	public XxlJobGroupDao xxlJobGroupDao;

	@Autowired
	private XxlJobRegistryDao xxlJobRegistryDao;

	@Autowired
	public XxlJobInfoDao xxlJobInfoDao;

	@Override
	public ReturnT<String> save(XxlJobGroup xxlJobGroup) {
		// valid
		if (!StringUtils.hasText(xxlJobGroup.getAppname())) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + "AppName"));
		}
		if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appname_length"));
		}
		if (xxlJobGroup.getAppname().contains(">") || xxlJobGroup.getAppname().contains("<")) {
			return new ReturnT<String>(500, "AppName" + I18nUtil.getString("system_unvalid"));
		}
		if (!StringUtils.hasText(xxlJobGroup.getTitle())) {
			return new ReturnT<String>(500,
					(I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
		}
		if (xxlJobGroup.getTitle().contains(">") || xxlJobGroup.getTitle().contains("<")) {
			return new ReturnT<String>(500,
					I18nUtil.getString("jobgroup_field_title") + I18nUtil.getString("system_unvalid"));
		}
		if (xxlJobGroup.getAddressType() != 0) {
			if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().length() == 0) {
				return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
			}
			if (xxlJobGroup.getAddressList().contains(">") || xxlJobGroup.getAddressList().contains("<")) {
				return new ReturnT<String>(500,
						I18nUtil.getString("jobgroup_field_registryList") + I18nUtil.getString("system_unvalid"));
			}

			String[] addresss = xxlJobGroup.getAddressList().split(",");
			for (String item : addresss) {
				if (item == null || item.trim().length() == 0) {
					return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
				}
			}
		}

		List<XxlJobGroup> pageList = xxlJobGroupDao.pageList(0, 1, xxlJobGroup.getAppname(), null);
		if (!CollectionUtils.isEmpty(pageList)) {
			return new ReturnT<>(ReturnT.FAIL_CODE, "the jobGroup appname could not by duplicate");
		}

		xxlJobGroup.setUpdateTime(new Date());
		int ret = xxlJobGroupDao.save(xxlJobGroup);
		return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
	}

	@Override
	public ReturnT<String> update(XxlJobGroup xxlJobGroup) {
		// valid
		if (!StringUtils.hasText(xxlJobGroup.getAppname())) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + "AppName"));
		}
		if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appname_length"));
		}
		if (!StringUtils.hasText(xxlJobGroup.getTitle())) {
			return new ReturnT<String>(500,
					(I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
		}
		if (xxlJobGroup.getAddressType() == 0) {
			// 0=自动注册
			List<String> registryList = findRegistryByAppName(xxlJobGroup.getAppname());
			String addressListStr = null;
			if (registryList != null && !registryList.isEmpty()) {
				Collections.sort(registryList);
				addressListStr = "";
				for (String item : registryList) {
					addressListStr += item + ",";
				}
				addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
			}
			xxlJobGroup.setAddressList(addressListStr);
		} else {
			// 1=手动录入
			if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().length() == 0) {
				return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
			}
			String[] addresss = xxlJobGroup.getAddressList().split(",");
			for (String item : addresss) {
				if (item == null || item.trim().length() == 0) {
					return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
				}
			}
		}
		// process
		xxlJobGroup.setUpdateTime(new Date());
		int ret = xxlJobGroupDao.update(xxlJobGroup);
		return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
	}

	private List<String> findRegistryByAppName(String appnameParam) {
		HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
		List<XxlJobRegistry> list = xxlJobRegistryDao.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
		if (list != null) {
			for (XxlJobRegistry item : list) {
				if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
					String appname = item.getRegistryKey();
					List<String> registryList = appAddressMap.get(appname);
					if (registryList == null) {
						registryList = new ArrayList<String>();
					}

					if (!registryList.contains(item.getRegistryValue())) {
						registryList.add(item.getRegistryValue());
					}
					appAddressMap.put(appname, registryList);
				}
			}
		}
		return appAddressMap.get(appnameParam);
	}

	@Override
	public ReturnT<String> remove(int id) {
		// valid
		int count = xxlJobInfoDao.pageListCount(0, 10, id, -1, null, null, null);
		if (count > 0) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_0"));
		}
		List<XxlJobGroup> allList = xxlJobGroupDao.findAll();
		if (allList.size() == 1) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_1"));
		}
		int ret = xxlJobGroupDao.remove(id);
		return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
	}

	@Override
	public ReturnT<XxlJobGroup> loadById(int id) {
		XxlJobGroup jobGroup = xxlJobGroupDao.load(id);
		return jobGroup != null ? new ReturnT<XxlJobGroup>(jobGroup)
				: new ReturnT<XxlJobGroup>(ReturnT.FAIL_CODE, null);
	}

	@Override
	public Map<String, Object> pageList(HttpServletRequest request, int start, int length, String appname,
			String title) {
		List<XxlJobGroup> list = xxlJobGroupDao.pageList(start, length, appname, title);
		int list_count = xxlJobGroupDao.pageListCount(start, length, appname, title);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("recordsTotal", list_count);
		maps.put("recordsFiltered", list_count); // 过滤后的总记录数
		maps.put("data", list); // 分页列表
		return maps;
	}
}