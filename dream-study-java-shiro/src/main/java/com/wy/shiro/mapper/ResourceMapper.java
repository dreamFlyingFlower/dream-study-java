package com.wy.shiro.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wy.shiro.entity.Resource;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

	/**
	 * 按父Id查询树
	 * 
	 * @param
	 * @return
	 */
	List<Resource> findResourceTreeVoByParentId(Map<String, Object> map);
}