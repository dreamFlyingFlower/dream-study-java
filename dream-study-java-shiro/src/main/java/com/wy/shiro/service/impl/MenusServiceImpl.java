package com.wy.shiro.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wy.shiro.constant.ResourceConstant;
import com.wy.shiro.constant.SuperConstant;
import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.vo.MenuVo;
import com.wy.shiro.mapper.MenusServiceMapper;
import com.wy.shiro.service.MenusService;
import com.wy.shiro.utils.CnCalendarUtil;
import com.wy.shiro.utils.ShiroUserUtil;

/**
 * 菜单服务实现
 *
 * @author 飞花梦影
 * @date 2022-06-22 00:11:38
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Service
public class MenusServiceImpl implements MenusService {

	@Value("${itheima.resource.systemcode}")
	private String systemCode;

	@Autowired
	private MenusServiceMapper menusServiceMapper;

	@Override
	public List<Resource> findTopLevel() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("resourceType", ResourceConstant.MENU);
		values.put("resourceIdList", ShiroUserUtil.getShiroUser().getResourceIds());
		values.put("isSystemRoot", SuperConstant.YES);
		values.put("enableFlag", SuperConstant.YES);
		values.put("systemCode", systemCode);
		return menusServiceMapper.findTopLevel(values);
	}

	@Override
	public List<MenuVo> findByResourceType(String parentId) {
		// 查询二级菜单
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("parentId", parentId);
		values.put("resourceType", ResourceConstant.MENU);
		values.put("resourceIdList", ShiroUserUtil.getShiroUser().getResourceIds());
		values.put("systemCode", systemCode);
		values.put("enableFlag", SuperConstant.YES);
		List<MenuVo> list = menusServiceMapper.findByResourceType(values);
		for (int i = 0; i < list.size(); i++) {
			values.put("parentId", list.get(i).getMenuid());
			List<MenuVo> iterableChildren = menusServiceMapper.findByResourceType(values);
			list.get(i).setMenus(iterableChildren);
		}
		return list;
	}

	@Override
	public Map<String, String> rollingTime() {
		Map<String, String> map = new HashMap<String, String>();
		String today = null;
		String lunar = null;
		String hourMinute = null;
		CnCalendarUtil cnCalendar = new CnCalendarUtil();
		lunar = cnCalendar.getNongli(new Date());
		String[] week = new String[] { "", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		int weekNum = calendar.get(Calendar.DAY_OF_WEEK);
		today = "今天是: " + sdf.format(calendar.getTime()) + "（农历：" + lunar + "） " + week[weekNum];
		sdf = new SimpleDateFormat("HH:mm:ss");
		hourMinute = sdf.format(calendar.getTime());
		map.put("today", today);
		map.put("hourMinute", hourMinute);
		return map;
	}

}
