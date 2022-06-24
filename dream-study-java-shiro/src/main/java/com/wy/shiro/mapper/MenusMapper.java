package com.wy.shiro.mapper;

import java.util.List;
import java.util.Map;

import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.vo.MenuVo;

/**
 * 菜单服务器层 Mapper
 * 
 * @author 飞花梦影
 * @date 2022-06-24 17:23:03
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface MenusMapper {

	/**
	 * 查询每个系统的顶级菜单
	 * 
	 * @param
	 * @return
	 */
	List<Resource> findTopLevel(Map<String, Object> map);

	/**
	 * 查询子菜单
	 * 
	 * @param
	 * @return
	 */
	List<MenuVo> findByResourceType(Map<String, Object> map);
}