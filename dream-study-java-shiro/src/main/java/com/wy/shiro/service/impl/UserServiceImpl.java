package com.wy.shiro.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wy.lang.StrTool;
import com.wy.shiro.constant.SuperConstant;
import com.wy.shiro.entity.Resource;
import com.wy.shiro.entity.Role;
import com.wy.shiro.entity.User;
import com.wy.shiro.entity.UserRole;
import com.wy.shiro.entity.vo.UserVo;
import com.wy.shiro.mapper.UserMapper;
import com.wy.shiro.mapper.UserRoleMapper;
import com.wy.shiro.service.UserService;
import com.wy.shiro.utils.DigestsUtil;
import com.wy.shiro.utils.ExceptionsUtil;
import com.wy.shiro.utils.ShiroUserUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户服务业务实现类
 *
 * @author 飞花梦影
 * @date 2022-06-22 00:13:45
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Override
	public User findUserByLoginName(String loginName) {
		List<User> userList =
				this.lambdaQuery().eq(User::getEnableFlag, SuperConstant.YES).eq(User::getLoginName, loginName).list();
		if (userList.size() == 1) {
			return userList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Role> findRoleByUserId(String userId) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("userId", userId);
		values.put("enableFlag", SuperConstant.YES);
		List<Role> list = userMapper.findRoleByUserId(values);
		return list;
	}

	@Override
	public List<Resource> findResourceByUserId(String userId) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("userId", userId);
		values.put("enableFlag", SuperConstant.YES);
		List<Resource> list = userMapper.findResourceByUserId(values);
		return list;
	}

	@Override
	public List<User> findUserList(UserVo userVo, Integer size, Integer pageIndex) {
		LambdaQueryChainWrapper<User> chainWrapper = this.generateCondition(userVo);
		Page<User> page = chainWrapper.orderByAsc(User::getSortNo).page(new Page<User>(pageIndex, size));
		return page.getRecords();
	}

	@Override
	public long countUserList(UserVo userVo) {
		return super.count(generateCondition(userVo));
	}

	private LambdaQueryChainWrapper<User> generateCondition(UserVo userVo) {
		return this.lambdaQuery()
				.eq(StrTool.isNotBlank(userVo.getLoginName()), User::getLoginName, userVo.getLoginName())
				.eq(StrTool.isNotBlank(userVo.getEmail()), User::getEmail, userVo.getEmail())
				.like(StrTool.isNotBlank(userVo.getRealName()), User::getRealName, userVo.getRealName());
	}

	@Override
	public UserVo getUserById(String id) {
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(super.getById(id), userVo);
		return userVo;
	}

	@Override
	@Transactional
	public Boolean saveOrUpdateUser(UserVo userVo) throws IllegalAccessException, InvocationTargetException {
		Boolean flag = true;
		try {
			if (StrTool.isNotBlank(userVo.getPlainPassword())) {
				entryptPassword(userVo);
			}
			User user = new User();
			BeanUtils.copyProperties(userVo, user);
			if (StrTool.isBlank(userVo.getId())) {
				user.setEnableFlag(SuperConstant.YES);
				userMapper.insert(user);
				userVo.setId(user.getId());

			} else {
				userMapper.updateById(user);
				userRoleMapper.delete(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, user.getId()));
			}
			bachUserRole(userVo);
		} catch (Exception e) {
			log.error("保存用户出错{}", ExceptionsUtil.getStackTraceAsString(e));
			flag = false;
		}
		return flag;
	}

	private void bachUserRole(UserVo userVo) {
		if (StrTool.isNotBlank(userVo.getHasRoleIds())) {
			List<UserRole> list = new ArrayList<>();
			List<String> roleIdList = Arrays.asList(userVo.getHasRoleIds().split(","));
			for (String roleId : roleIdList) {
				UserRole userRole = new UserRole();
				userRole.setUserId(userVo.getId());
				userRole.setRoleId(roleId);
				userRole.setEnableFlag(SuperConstant.YES);
				list.add(userRole);
			}
			userRoleMapper.batchInsert(list);
		}
	}

	@Override
	public Boolean getUserByLoginNameOrMobilOrEmail(String loginName) {
		User user = this.getUserIdByLoginNameOrMobilOrEmail(loginName);
		return Objects.isNull(user);
	}

	@Override
	public User getUserIdByLoginNameOrMobilOrEmail(String loginName) {
		List<User> list = this.lambdaQuery().or(t -> t.eq(User::getLoginName, loginName))
				.or(t -> t.eq(User::getMobil, loginName)).or(t -> t.eq(User::getEmail, loginName)).list();
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Boolean updateByIds(List<String> list, String enableFlag) {
		return this.lambdaUpdate().set(User::getEnableFlag, enableFlag).in(User::getId, list).update();
	}

	@Override
	public void entryptPassword(UserVo userVo) {
		Map<String, String> map = DigestsUtil.entryptPassword(userVo.getPlainPassword());
		userVo.setSalt(map.get("salt"));
		userVo.setPassWord(map.get("password"));
	}

	@Override
	public List<String> findUserHasRoleIds(String id) {
		List<UserRole> userRoleList = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
				.eq(UserRole::getUserId, id).eq(UserRole::getEnableFlag, SuperConstant.YES));
		List<String> list = new ArrayList<>();
		userRoleList.forEach(n -> list.add(n.getRoleId()));
		return list;
	}

	@Override
	public Boolean saveNewPassword(String oldPassword, String plainPassword)
			throws IllegalAccessException, InvocationTargetException {
		// user对象
		User user = super.getById(ShiroUserUtil.getShiroUserId());
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(userVo, user);
		// 对user中的salt进行散列
		oldPassword = DigestsUtil.sha1(oldPassword, user.getSalt());
		if (!user.getPassWord().equals(oldPassword)) {
			return false;
		}
		userVo.setPlainPassword(plainPassword);
		entryptPassword(userVo);
		try {
			user.setPassWord(userVo.getPassWord());
			user.setSalt(userVo.getSalt());
			userMapper.updateById(user);
			return true;
		} catch (Exception e) {
			log.error("更新用户密码失败：{}", ExceptionsUtil.getStackTraceAsString(e));
			return false;
		}
	}
}