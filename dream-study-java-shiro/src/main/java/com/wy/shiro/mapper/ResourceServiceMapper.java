package com.wy.shiro.mapper;

import java.util.List;
import java.util.Map;

import com.wy.shiro.entity.Resource;

/**
 * 资源服务
 */
public interface ResourceServiceMapper {

	/**
	 * @Description 按父Id查询树
	 * @param
	 * @return
	 */
	List<Resource> findResourceTreeVoByParentId(Map<String, Object> map);
}