package com.wy.crl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 大屏预览入口
 *
 * @author 飞花梦影
 * @date 2022-10-18 23:29:36
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Controller
@RequestMapping("/test/bigScreen/templat")
public class BigScreenCrl {

	/**
	 * 生产销售监控模版
	 * 
	 * @param modelAndView
	 * @return
	 */
	@GetMapping("/index1")
	public ModelAndView index1(ModelAndView modelAndView) {
		modelAndView.setViewName("/bigscreen/template1/index");
		return modelAndView;
	}

	/**
	 * 智慧物流监控模版
	 * 
	 * @param modelAndView
	 * @return
	 */
	@GetMapping("/index2")
	public ModelAndView index2(ModelAndView modelAndView) {
		modelAndView.setViewName("/bigscreen/template2/index");
		return modelAndView;
	}
}