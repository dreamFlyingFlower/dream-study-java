package com.wy.shiro.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wy.shiro.entity.Role;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

	int batchInsert(List<Role> list);

	/**
	 * 查询角色拥有的资源Id字符串
	 */
	List<String> findRoleHasResourceIds(Map<String, Object> map);

	/**
	 * 查询任务角色
	 */
	List<Role> findRoleDetailByLenderId(Map<String, Object> map);
}