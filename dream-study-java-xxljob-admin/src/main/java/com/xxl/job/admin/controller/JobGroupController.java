package com.xxl.job.admin.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.service.JobGroupService;
import com.xxl.job.core.biz.model.ReturnT;

/**
 * job group controller
 * 
 * @author xuxueli 2016-10-02 20:52:56
 */
@Controller
@RequestMapping("/jobgroup")
public class JobGroupController {

	@Autowired
	private JobGroupService jobGroupService;

	@RequestMapping
	public String index(Model model) {
		return "jobgroup/jobgroup.index";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") int start,
			@RequestParam(required = false, defaultValue = "10") int length, String appname, String title) {
		return jobGroupService.pageList(request, start, length, appname, title);
	}

	@RequestMapping("/save")
	@ResponseBody
	public ReturnT<String> save(XxlJobGroup xxlJobGroup) {
		return jobGroupService.save(xxlJobGroup);
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlJobGroup xxlJobGroup) {
		return jobGroupService.update(xxlJobGroup);
	}

	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(int id) {
		return jobGroupService.remove(id);
	}

	@RequestMapping("/loadById")
	@ResponseBody
	public ReturnT<XxlJobGroup> loadById(int id) {
		return jobGroupService.loadById(id);
	}
}