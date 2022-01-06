package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.result.Result;
import com.wy.service.XxlJobService;

@RequestMapping("xxlJob")
@RestController
public class XxlJobCrl {

	@Autowired
	private XxlJobService xxljobService;

	/**
	 * 通过HTTP请求添加定时任务
	 * 
	 * @return
	 */
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

	@GetMapping("trigger")
	public Result<?> trigger(Integer id) {
		return xxljobService.trigger(id, null, null);
	}
}