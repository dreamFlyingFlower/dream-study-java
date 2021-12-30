package com.wy.crl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 通用访问拦截匹配
 * 
 * @author 飞花梦影
 * @date 2021-12-29 23:32:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Controller
public class IndexCrl {

	/**
	 * 页面跳转
	 * 
	 * @param module
	 * @param url
	 * @return
	 */
	@RequestMapping("{url}.html")
	public String page(@PathVariable("url") String url) {
		return url;
	}

	/**
	 * 页面跳转(一级目录)
	 * 
	 * @param module
	 * @param function
	 * @param url
	 * @return
	 */
	@RequestMapping("{module}/{url}.html")
	public String page(@PathVariable("module") String module, @PathVariable("url") String url) {
		return module + "/" + url;
	}

	/**
	 * 页面跳转（二级目录)
	 * 
	 * @param module
	 * @param url
	 * @return String
	 */
	@RequestMapping("{module}/{sub}/{url}.html")
	public String page(@PathVariable("module") String module, @PathVariable("sub") String sub,
			@PathVariable("url") String url) {
		return module + "/" + sub + "/" + url;
	}
}