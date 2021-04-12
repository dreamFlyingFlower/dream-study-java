package com.wy.crl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试类
 * 
 * @author ParadiseWY
 * @date 2020-11-18 13:42:50
 * @git {@link https://github.com/mygodness100}
 */
@Controller
@RequestMapping("index")
public class Index {

	@GetMapping("index")
	public Object index() {
		return "index";
	}
}