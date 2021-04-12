package com.wy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Role;

/**
 * 角色表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role, Long> {

	/**
	 * 根据用户编号获取角色列表
	 * 
	 * @param userId 用户编号
	 * @return 用户角色列表
	 */
	List<Role> selectByUserId(Long userId);
}