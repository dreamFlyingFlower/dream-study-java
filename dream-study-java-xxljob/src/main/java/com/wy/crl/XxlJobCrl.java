package com.wy.crl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("xxlJob")
@RestController
public class XxlJobCrl {

	/**
	 * 通过HTTP请求添加定时任务
	 * 
	 * @return
	 */
	@PostMapping("add")
	public String add() {
		return "add";
	}
}