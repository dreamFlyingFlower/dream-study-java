package com.wy.shiro.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.wy.shiro.entity.Role;
import com.wy.shiro.entity.vo.ComboboxVo;
import com.wy.shiro.entity.vo.RoleVo;

/**
 * 角色service接口层
 */
public interface RoleService {

	/**
	 * 角色的分页查询
	 */
	List<Role> findRoleList(RoleVo roleVo, Integer rows, Integer page);

	/**
	 * 统计角色的分页查询
	 */
	long countRoleList(RoleVo role);

	/**
	 * 按Id查询角色
	 */
	RoleVo getRoleById(String id);

	/**
	 * 保存或更新角色
	 */
	boolean saveOrUpdateRole(RoleVo roleVo) throws IllegalAccessException, InvocationTargetException;

	/**
	 * 角色删除
	 * 
	 * @param
	 * @return
	 */
	Boolean updateByIds(List<String> list, String enableFlag);

	/**
	 * 根据角色标志查询角色
	 */
	Role findRoleByLable(String lable);

	/**
	 * 查询有效角色下拉列表
	 */
	List<ComboboxVo> findRoleComboboxVo(String roleIds);

	/**
	 * 查询角色拥有的资源Id字符串
	 */
	List<String> findRoleHasResourceIds(String id);
}