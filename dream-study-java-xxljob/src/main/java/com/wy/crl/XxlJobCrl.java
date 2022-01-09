package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.result.Result;
import com.wy.service.XxlJobGroupService;
import com.wy.service.XxlJobInfoService;
import com.wy.service.XxlJobService;

@RequestMapping("xxlJob")
@RestController
public class XxlJobCrl {

	@Autowired
	private XxlJobInfoService xxlJobInfoService;

	@Autowired
	private XxlJobService xxlJobService;

	@Autowired
	private XxlJobGroupService xxlJobGroupService;

	@GetMapping("login")
	public String login() {
		xxlJobService.login();
		return null;
	}

	@GetMapping("getJobGroup")
	public String getJobGroup() {
		xxlJobGroupService.getJobGroup(null);
		return null;
	}

	@GetMapping("add")
	public String add() {
		xxlJobInfoService.add(null);
		return "add";
	}

	@GetMapping("remove/{id}")
	public String remove(@PathVariable Integer id) {
		xxlJobInfoService.remove(id);
		return null;
	}

	@GetMapping("removeByName")
	public String removeByName(String groupName, String executorHandler) {
		xxlJobInfoService.removeByName(groupName, executorHandler);
		return null;
	}

	@GetMapping("start/{id}")
	public String start(@PathVariable Integer id) {
		xxlJobInfoService.start(id);
		return null;
	}

	@GetMapping("startByName")
	public String startByName(String groupName, String executorHandler) {
		xxlJobInfoService.startByName(groupName, executorHandler);
		return null;
	}

	@GetMapping("stop/{id}")
	public String stop(@PathVariable Integer id) {
		xxlJobInfoService.stop(id);
		return null;
	}

	@GetMapping("stopByName")
	public String stopByName(String groupName, String executorHandler) {
		xxlJobInfoService.stopByName(groupName, executorHandler);
		return null;
	}

	@GetMapping("update")
	public String update() {
		xxlJobInfoService.update(null);
		return null;
	}

	@GetMapping("trigger")
	public Result<?> trigger(Integer id) {
		return xxlJobInfoService.trigger(id, null, null);
	}

	@GetMapping("triggerByName")
	public String triggerByName(String groupName, String executorHandler) {
		xxlJobInfoService.triggerByName(groupName, executorHandler, null, null);
		return null;
	}
}