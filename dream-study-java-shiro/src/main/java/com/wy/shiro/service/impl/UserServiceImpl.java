package com.wy.shiro.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.lang.StrTool;
import com.wy.shiro.constant.SuperConstant;
import com.wy.shiro.core.adapter.UserAdapter;
import com.wy.shiro.entity.User;
import com.wy.shiro.entity.UserExample;
import com.wy.shiro.entity.UserRole;
import com.wy.shiro.entity.UserRoleExample;
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
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	UserAdapter userAdapter;

	@Override
	public List<User> findUserList(UserVo userVo, Integer rows, Integer page) {
		UserExample userExample = this.userListExample(userVo);
		userExample.setOrderByClause("sort_no asc ");
		userExample.setPage(page);
		userExample.setRow(rows);
		return userMapper.selectByExample(userExample);
	}

	@Override
	public long countUserList(UserVo userVo) {
		UserExample userExample = this.userListExample(userVo);
		return userMapper.countByExample(userExample);
	}

	private UserExample userListExample(UserVo userVo) {
		UserExample userExample = new UserExample();
		UserExample.Criteria criteria = userExample.createCriteria();
		if (StrTool.isNotBlank(userVo.getLoginName())) {
			criteria.andLoginNameEqualTo(userVo.getLoginName());
		}
		if (StrTool.isNotBlank(userVo.getRealName())) {
			criteria.andRealNameLike(userVo.getRealName());
		}
		if (StrTool.isNotBlank(userVo.getEmail())) {
			criteria.andEmailEqualTo(userVo.getEmail());
		}
		return userExample;
	}

	@Override
	public UserVo getUserById(String id) {
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(userMapper.selectByPrimaryKey(id, null), userVo);
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
				userMapper.updateByPrimaryKey(user);
				UserRoleExample userRoleExample = new UserRoleExample();
				userRoleExample.createCriteria().andUserIdEqualTo(user.getId());
				userRoleMapper.deleteByExample(userRoleExample);
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
		UserExample userExample = new UserExample();
		UserExample.Criteria criteria0 = userExample.createCriteria().andLoginNameEqualTo(loginName);
		UserExample.Criteria criteria1 = userExample.createCriteria().andMobilEqualTo(loginName);
		UserExample.Criteria criteria2 = userExample.createCriteria().andEmailEqualTo(loginName);
		userExample.or(criteria0);
		userExample.or(criteria1);
		userExample.or(criteria2);
		List<User> list = userMapper.selectByExample(userExample);
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Boolean updateByIds(List<String> list, String enableFlag) {
		UserExample userExample = new UserExample();
		userExample.createCriteria().andIdIn(list);
		User userHandler = new User();
		userHandler.setEnableFlag(enableFlag);
		int flag = userMapper.updateByExampleSelective(userHandler, userExample);
		if (flag > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void entryptPassword(UserVo userVo) {
		Map<String, String> map = DigestsUtil.entryptPassword(userVo.getPlainPassword());
		userVo.setSalt(map.get("salt"));
		userVo.setPassWord(map.get("password"));
	}

	@Override
	public List<String> findUserHasRoleIds(String id) {
		UserRoleExample userRoleExample = new UserRoleExample();
		userRoleExample.createCriteria().andUserIdEqualTo(id).andEnableFlagEqualTo(SuperConstant.YES);
		List<UserRole> userRoleList = userRoleMapper.selectByExample(userRoleExample);
		List<String> list = new ArrayList<>();
		userRoleList.forEach(n -> list.add(n.getRoleId()));
		return list;
	}

	@Override
	public Boolean saveNewPassword(String oldPassword, String plainPassword)
			throws IllegalAccessException, InvocationTargetException {
		// user对象
		User user = userMapper.selectByPrimaryKey(ShiroUserUtil.getShiroUserId(), null);
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
			userMapper.updateByPrimaryKey(user);
			return true;
		} catch (Exception e) {
			log.error("更新用户密码失败：{}", ExceptionsUtil.getStackTraceAsString(e));
			return false;
		}
	}
}