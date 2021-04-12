package com.wy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Depart;

/**
 * 部门表
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Mapper
public interface DepartMapper extends BaseMapper<Depart, Long> {

	/**
	 * 根据角色编号查询该角色所属部门
	 * 
	 * @param roleId 角色编号
	 * @return 部门集合
	 */
	List<Depart> selectByRoleId(Long roleId);
}