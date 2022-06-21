package com.wy.retry.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wy.retry.pojo.Resource;
import com.wy.retry.pojo.ResourceExample;

@Mapper
public interface ResourceMapper {

	long countByExample(ResourceExample example);

	int deleteByExample(ResourceExample example);

	int deleteByPrimaryKey(String id);

	int insert(Resource record);

	int insertSelective(Resource record);

	List<Resource> selectByExample(ResourceExample example);

	Resource selectByPrimaryKey(@Param("id") String id, @Param("resultColumn") String resultColumn);

	int updateByExampleSelective(@Param("record") Resource record, @Param("example") ResourceExample example);

	int updateByExample(@Param("record") Resource record, @Param("example") ResourceExample example);

	int updateByPrimaryKeySelective(Resource record);

	int updateByPrimaryKey(Resource record);

	int batchInsert(List<Resource> list);
}