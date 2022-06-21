package com.wy.shiro.mapper;

import java.util.List;
import java.util.Map;

import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.vo.MenuVo;

/**
 * @Description 菜单服务器层 Mapper
 */

public interface MenusServiceMapper {

	/**
	 * 查询每个系统的顶级菜单
	 * 
	 * @param
	 * @return
	 */
	public List<Resource> findTopLevel(Map<String, Object> map);

	/**
	 * 查询子菜单
	 * 
	 * @param
	 * @return
	 */
	public List<MenuVo> findByResourceType(Map<String, Object> map);
}