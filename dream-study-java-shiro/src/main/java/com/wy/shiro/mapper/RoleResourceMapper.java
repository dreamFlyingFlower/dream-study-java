package com.wy.shiro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wy.shiro.entity.RoleResource;
import com.wy.shiro.entity.RoleResourceExample;

@Mapper
public interface RoleResourceMapper {

	long countByExample(RoleResourceExample example);

	int deleteByExample(RoleResourceExample example);

	int deleteByPrimaryKey(String id);

	int insert(RoleResource record);

	int insertSelective(RoleResource record);

	List<RoleResource> selectByExample(RoleResourceExample example);

	RoleResource selectByPrimaryKey(@Param("id") String id, @Param("resultColumn") String resultColumn);

	int updateByExampleSelective(@Param("record") RoleResource record, @Param("example") RoleResourceExample example);

	int updateByExample(@Param("record") RoleResource record, @Param("example") RoleResourceExample example);

	int updateByPrimaryKeySelective(RoleResource record);

	int updateByPrimaryKey(RoleResource record);

	int batchInsert(List<RoleResource> list);
}