package com.wy.shiro.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.Role;
import com.wy.shiro.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

	int batchInsert(List<User> list);

	/**
	 * 按用户Id查找对应角色
	 * 
	 * @param
	 * @return
	 */
	List<Role> findRoleByUserId(Map<String, Object> map);

	/**
	 * 按用户Id查找对应资源
	 * 
	 * @param
	 * @return
	 */
	List<Resource> findResourceByUserId(Map<String, Object> map);
}