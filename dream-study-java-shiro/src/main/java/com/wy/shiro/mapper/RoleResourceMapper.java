package com.wy.shiro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wy.shiro.entity.RoleResource;

@Mapper
public interface RoleResourceMapper extends BaseMapper<RoleResource> {

	int batchInsert(List<RoleResource> list);
}