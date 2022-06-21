package com.wy.retry.service;

import java.util.List;
import java.util.Map;

import com.wy.retry.pojo.Resource;
import com.wy.retry.vo.MenuVo;

/**
 * 菜单服务
 */
public interface MenusService {

	/**
	 * 查询子菜单
	 * 
	 * @param
	 * @return
	 */
	List<MenuVo> findByResourceType(String parentId);

	/**
	 * 时间滚动
	 * 
	 * @param
	 * @return
	 */
	Map<String, String> rollingTime();

	/**
	 * 查询每个系统的顶级菜单
	 * 
	 * @param
	 * @return
	 */
	List<Resource> findTopLevel();
}