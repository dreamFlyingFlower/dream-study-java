package com.wy.crl;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Freemarker模板测试,resources/templates/free是模板目录
 * 
 * 不可使用RestController注解,在解析时会因为Json解析而找不到指定的页面
 * 
 * ModelMap:该类可以携带数据到页面中,是一个继承Map的类
 * 
 * Freemarker更多的使用语法可百度
 * 
 * @author ParadiseWY
 * @date 2020-11-19 14:00:12
 * @git {@link https://github.com/mygodness100}
 */
@Controller
@RequestMapping("freemarker")
public class FreemarkerCrl {

	@GetMapping("index")
	public String index(ModelMap map) {
		// 添加数据到页面中,在页面中使用:${key},在此处就是${test},可以在任意地方使用
		map.put("test", "欢迎来到Freemarker的世界");
		// 对象可以使用object.name的方式在模板中使用
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", "godness");
		map.put("user", hashMap);
		// 返回页面地址,不需要加后缀,配置文件会自动添加
		return "free/index";
	}
}