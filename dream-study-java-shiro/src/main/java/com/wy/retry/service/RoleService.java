package com.wy.retry.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.wy.retry.pojo.Role;
import com.wy.retry.vo.ComboboxVo;
import com.wy.retry.vo.RoleVo;

/**
 * 角色service接口层
 */
public interface RoleService {

	/**
	 * @Description 角色的分页查询
	 * @param
	 * @return
	 */
	public List<Role> findRoleList(RoleVo roleVo, Integer rows, Integer page);

	/**
	 * @Description 统计角色的分页查询
	 * @param
	 * @return
	 */
	public long countRoleList(RoleVo role);

	/**
	 * @Description 按Id查询角色
	 * @param
	 * @return
	 */
	public RoleVo getRoleById(String id);

	/**
	 * @Description 保存或更新角色
	 * @param
	 * @return
	 */
	public boolean saveOrUpdateRole(RoleVo roleVo) throws IllegalAccessException, InvocationTargetException;

	/**
	 * @Description 角色删除
	 * @param
	 * @return
	 */
	public Boolean updateByIds(List<String> list, String enableFlag);

	/**
	 * @Description 根据角色标志查询角色
	 * @param
	 * @return
	 */
	public Role findRoleByLable(String lable);

	/**
	 * @Description 查询有效角色下拉列表
	 * @param
	 * @return
	 */
	public List<ComboboxVo> findRoleComboboxVo(String roleIds);

	/**
	 * @Description 查询角色拥有的资源Id字符串
	 * @param
	 * @return
	 */
	public List<String> findRoleHasResourceIds(String id);
}