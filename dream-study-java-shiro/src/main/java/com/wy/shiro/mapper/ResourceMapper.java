package com.wy.shiro.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wy.shiro.entity.Resource;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

	// long countByExample(ResourceExample example);
	//
	// int deleteByExample(ResourceExample example);
	//
	// int deleteByPrimaryKey(String id);
	//
	// int insert(Resource record);
	//
	// int insertSelective(Resource record);
	//
	// List<Resource> selectByExample(ResourceExample example);
	//
	// Resource selectByPrimaryKey(@Param("id") String id, @Param("resultColumn")
	// String resultColumn);
	//
	// int updateByExampleSelective(@Param("record") Resource record,
	// @Param("example") ResourceExample example);
	//
	// int updateByExample(@Param("record") Resource record, @Param("example")
	// ResourceExample example);
	//
	// int updateByPrimaryKeySelective(Resource record);
	//
	// int updateByPrimaryKey(Resource record);
	//
	// int batchInsert(List<Resource> list);
}