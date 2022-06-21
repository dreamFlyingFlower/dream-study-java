package com.wy.retry.mappercustom;

import java.util.List;
import java.util.Map;

import com.wy.retry.pojo.Resource;

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