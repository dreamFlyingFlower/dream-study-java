package com.wy.crl;

import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Thymeleaf并不适合特别复杂的页面,相比Freemarker更轻量级
 * 
 * @author ParadiseWY
 * @date 2020-11-20 10:21:13
 * @git {@link https://github.com/mygodness100}
 */
@Controller
@RequestMapping("thymeleaf")
public class ThymeleafCrl {

	@GetMapping("index")
	public String index(ModelMap map, HttpServletRequest request) {
		if (StringUtils.hasText(request.getParameter("test"))) {
			throw new RuntimeException();
		}
		// 添加数据到页面中,在页面中使用只能在标签中使用,较Freemarker更为麻烦
		map.put("test", "欢迎来到Thymeleaf的世界");
		map.put("test1", 12);
		map.put("test2", "13");
		map.put("test3", "testId");
		map.put("test4", "<h6>不会转义特殊字符串,所以这串字比较小</h6>");
		map.put("foreach", Arrays.asList("ssss", "fdff", "fdsfdsg"));
		// 对象可以使用object.name的方式在模板中使用
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", "godness Beilu");
		map.put("user", hashMap);
		// 返回页面地址,不需要加后缀,配置文件会自动添加
		return "thy/index";
	}
}