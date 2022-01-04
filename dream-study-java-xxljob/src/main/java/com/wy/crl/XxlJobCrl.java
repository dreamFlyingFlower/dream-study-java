package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.service.XxljobService;

@RequestMapping("xxlJob")
@RestController
public class XxlJobCrl {

	@Autowired
	private XxljobService xxljobService;

	/**
	 * 通过HTTP请求添加定时任务
	 * 
	 * @return
	 */
	@PostMapping("add")
	public String add() {
		return "add";
	}

	@GetMapping("login")
	public String login() {
		xxljobService.login();
		return null;
	}
	
	@GetMapping("pageList")
	public void pageList(){
		xxljobService.pageList();
	}
}