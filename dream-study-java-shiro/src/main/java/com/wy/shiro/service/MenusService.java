package com.wy.shiro.service;

import java.util.List;
import java.util.Map;

import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.vo.MenuVo;

/**
 * 菜单服务接口
 *
 * @author 飞花梦影
 * @date 2022-06-22 00:06:40
 * @git {@link https://gitee.com/dreamFlyingFlower}
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