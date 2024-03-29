package com.wy.crl;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 测试Crl
 * 
 * @author 飞花梦影
 * @date 2022-09-20 14:54:46
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@RestController
@RequestMapping("test")
@SessionAttributes("message")
public class TestCrl {

	@GetMapping("testHeader")
	public void testHeader(@RequestHeader("content-type") String contentType,
			@CookieValue("jessionid") String jessionid) {
		System.out.println(contentType);
		System.out.println(jessionid);
	}

	/**
	 * 在其他方法执行之前,先执行当前方法
	 * 
	 * @param name
	 * @return
	 */
	@ModelAttribute("username")
	public void showModel(String name, Model model) {
		System.out.println("showModel method name is " + name);
		name = name + "aaa";
		model.addAttribute("username", name);
	}

	/**
	 * 从Model中取出username赋值给name
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/useModelAttribute")
	public String useModelAttribute(@ModelAttribute("username") String name) {
		System.out.println("controller method name is " + name);
		return "success";
	}

	@GetMapping("testSession")
	public String testSession(Model model) {
		// 在类上添加了 SessionAttributes,会将message存入到session中,服务器和客户端都会存
		model.addAttribute("message", "测试数据session");
		return "success";
	}

	/**
	 * 通过SessionAttribute从session中取数据
	 * 
	 * @param message
	 * @return
	 */
	@GetMapping("testSession1")
	public String testSession1(@SessionAttribute("message") String message) {
		return "success";
	}

	/**
	 * 使用ANT风格(通配符)
	 * 
	 * <pre>
	 * ?: 匹配单个任意字符,但是对特殊符号无效,如?,/
	 * *: 匹配任意多个字符,特殊符号无效,如/
	 * **: 直接匹配任意多层目录,但是中间或两边不能有任何其他字符,否则原样处理.如/**\/test可匹配/a/b/test,/a/test/
	 * /a**b\/test只能匹配/a**b/test
	 * </pre>
	 */
	// @GetMapping("a?a/test")
	// @GetMapping("a*a/test")
	@GetMapping("/**/test")
	public void testPattern() {

	}
}