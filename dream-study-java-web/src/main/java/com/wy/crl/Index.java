package com.wy.crl;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wy.config.MessageService;

/**
 * 测试类
 * 
 * @author ParadiseWY
 * @date 2020-11-18 13:42:50
 * @git {@link https://github.com/mygodness100}
 */
@Controller
@RequestMapping("test")
public class Index {

	@GetMapping("index")
	public Object index() {
		System.out.println(MessageService.getMessage("bundle.super_admin"));
		System.out.println(MessageService.getMessage("bundle.super_admin", Locale.US));
		return "index";
	}
}