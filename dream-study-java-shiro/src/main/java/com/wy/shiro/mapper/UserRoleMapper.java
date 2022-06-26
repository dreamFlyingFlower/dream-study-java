package com.wy.shiro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wy.shiro.entity.UserRole;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

	int batchInsert(List<UserRole> list);
}