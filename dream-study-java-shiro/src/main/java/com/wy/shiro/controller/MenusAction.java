package com.wy.shiro.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.shiro.core.base.ShiroUser;
import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.vo.MenuVo;
import com.wy.shiro.service.MenusService;
import com.wy.shiro.utils.ShiroUserUtil;

/**
 * 菜单首页管理
 */
@Controller
@RequestMapping(value = "/menus")
public class MenusAction {

	@Autowired
	private MenusService menusService;

	/**
	 * @description: 加载系统
	 * @return
	 */
	@RequestMapping(value = "/system")
	public ModelAndView system() {
		ModelAndView modelAndVie = new ModelAndView("/menus/menus-system");
		Map<String, Object> map = new HashMap<String, Object>();
		ShiroUser shiroUser = ShiroUserUtil.getShiroUser();
		map.put("currentUser", shiroUser);
		List<Resource> list = menusService.findTopLevel();
		map.put("list", list);
		map.putAll(menusService.rollingTime());
		modelAndVie.addAllObjects(map);
		return modelAndVie;
	}

	/**
	 * @description: 首页信息
	 */
	@RequestMapping(value = "/home")
	public ModelAndView home() {
		return new ModelAndView("/menus/menus-home");
	}

	/**
	 * @description: 获得所有菜单
	 */
	@RequestMapping(value = "/findAllMenu")
	@ResponseBody
	public List<MenuVo> findAllMenu(@RequestParam("id") String id) {
		return menusService.findByResourceType(id);
	}

}
