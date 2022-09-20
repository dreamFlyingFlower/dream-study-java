package com.wy.crl;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;

/**
 * 测试Crl
 * 
 * {@link RequestHeader}:从请求头获得值,如charset,content-type等,由{@link RequestHeaderMethodArgumentResolver}解析
 * {@link CookieValue}:从请求中获取cookie值,由{@link ServletCookieValueMethodArgumentResolver}解析
 * {@link ModelAttribute}:
 *
 * @author 飞花梦影
 * @date 2022-09-20 14:54:46
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@RestController
@RequestMapping("test")
public class TestCrl {

	@GetMapping("testHeader")
	public void testHeader(@RequestHeader("content-type") String contentType,
	        @CookieValue("jessionid") String jessionid) {
		System.out.println(contentType);
		System.out.println(jessionid);
	}

	@ModelAttribute("username")
	public String showModel(String name) {
		System.out.println("showModel method name is " + name);
		name = name + "aaa";
		return name;
	}

	@RequestMapping("/useModelAttribute")
	public String useModelAttribute(@ModelAttribute("username") String name) {
		System.out.println("controller method name is " + name);
		return "success";
	}
}