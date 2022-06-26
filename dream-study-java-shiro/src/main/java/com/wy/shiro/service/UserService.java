package com.wy.shiro.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.Role;
import com.wy.shiro.entity.User;
import com.wy.shiro.entity.vo.UserVo;

/**
 * 用户服务层
 */
public interface UserService {

	/**
	 * 按用户名查找用户
	 * 
	 * @param loginName 登录名
	 */
	User findUserByLoginName(String loginName);

	/**
	 * 查找用户所有角色
	 * 
	 * @param userId 用户Id
	 */
	List<Role> findRoleByUserId(String userId);

	/**
	 * 查询用户有那些资源
	 * 
	 * @param userId 用户Id
	 */
	List<Resource> findResourceByUserId(String userId);

	/**
	 * 分页查询
	 */
	List<User> findUserList(UserVo userVo, Integer rows, Integer page);

	/**
	 * count分页查询
	 */
	long countUserList(UserVo userVo);

	/**
	 * 根据id查询用户
	 */
	UserVo getUserById(String id);

	/**
	 * 新增,修改对象
	 */
	Boolean saveOrUpdateUser(UserVo userVo) throws IllegalAccessException, InvocationTargetException;

	/**
	 * 验证用户是否存在
	 */
	Boolean getUserByLoginNameOrMobilOrEmail(String loginName);

	/**
	 * 通过登录名手机号电子邮箱地址获取user
	 */
	User getUserIdByLoginNameOrMobilOrEmail(String loginName);

	/**
	 * 伪删除用户
	 */
	Boolean updateByIds(List<String> list, String enableFlag);

	/**
	 * 密码加密
	 */
	void entryptPassword(UserVo userVo);

	/**
	 * 用户拥有的角色
	 */
	List<String> findUserHasRoleIds(String id);

	/**
	 * 修改密码
	 */
	Boolean saveNewPassword(String oldPassword, String plainPassword)
			throws IllegalAccessException, InvocationTargetException;
}