package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.result.Result;
import com.wy.service.XxlJobService;

@RequestMapping("xxlJob")
@RestController
public class XxlJobCrl {

	@Autowired
	private XxlJobService xxljobService;

	@GetMapping("add")
	public String add() {
		xxljobService.add(null);
		return "add";
	}

	@GetMapping("login")
	public String login() {
		xxljobService.login();
		return null;
	}

	@GetMapping("remove/{id}")
	public String remove(@PathVariable Integer id) {
		xxljobService.remove(id);
		return null;
	}

	@GetMapping("removeByName")
	public String removeByName(String groupName, String executorHandler) {
		xxljobService.removeByName(groupName, executorHandler);
		return null;
	}

	@GetMapping("start/{id}")
	public String start(@PathVariable Integer id) {
		xxljobService.start(id);
		return null;
	}

	@GetMapping("startByName")
	public String startByName(String groupName, String executorHandler) {
		xxljobService.startByName(groupName, executorHandler);
		return null;
	}

	@GetMapping("stop/{id}")
	public String stop(@PathVariable Integer id) {
		xxljobService.stop(id);
		return null;
	}

	@GetMapping("stopByName")
	public String stopByName(String groupName, String executorHandler) {
		xxljobService.stopByName(groupName, executorHandler);
		return null;
	}

	@GetMapping("update")
	public String update() {
		xxljobService.update(null);
		return null;
	}

	@GetMapping("trigger")
	public Result<?> trigger(Integer id) {
		return xxljobService.trigger(id, null, null);
	}

	@GetMapping("triggerByName")
	public String triggerByName(String groupName, String executorHandler) {
		xxljobService.triggerByName(groupName, executorHandler, null, null);
		return null;
	}
}