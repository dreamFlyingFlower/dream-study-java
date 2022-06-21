package com.wy.shiro.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wy.lang.StrTool;
import com.wy.shiro.constant.SuperConstant;
import com.wy.shiro.entity.Role;
import com.wy.shiro.entity.RoleExample;
import com.wy.shiro.entity.RoleResource;
import com.wy.shiro.entity.RoleResourceExample;
import com.wy.shiro.entity.vo.ComboboxVo;
import com.wy.shiro.entity.vo.RoleVo;
import com.wy.shiro.mapper.RoleMapper;
import com.wy.shiro.mapper.RoleResourceMapper;
import com.wy.shiro.mapper.RoleServiceMapper;
import com.wy.shiro.service.RoleService;
import com.wy.shiro.utils.ExceptionsUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 角色服务业务实现类
 *
 * @author 飞花梦影
 * @date 2022-06-22 00:12:47
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Service
@Slf4j
public class RoleServiceImp implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private RoleResourceMapper roleResourceMapper;

	@Autowired
	private RoleServiceMapper roleServiceMapper;

	@Override
	public List<Role> findRoleList(RoleVo roleVo, Integer rows, Integer page) {
		RoleExample roleExample = this.roleListExample(roleVo);
		roleExample.setPage(page);
		roleExample.setRow(rows);
		roleExample.setOrderByClause(" SORT_NO ASC");
		return roleMapper.selectByExample(roleExample);
	}

	@Override
	public long countRoleList(RoleVo roleVo) {
		RoleExample roleExample = this.roleListExample(roleVo);
		return roleMapper.countByExample(roleExample);
	}

	private RoleExample roleListExample(RoleVo roleVo) {
		RoleExample roleExample = new RoleExample();
		RoleExample.Criteria criteria = roleExample.createCriteria();
		if (StrTool.isNotBlank(roleVo.getRoleName())) {
			criteria.andRoleNameLike(roleVo.getRoleName());
		}

		if (StrTool.isNotBlank(roleVo.getLabel())) {
			criteria.andLabelEqualTo(roleVo.getLabel());
		}
		return roleExample;
	}

	@Override
	public RoleVo getRoleById(String id) {
		RoleVo roleVo = new RoleVo();
		BeanUtils.copyProperties(roleMapper.selectByPrimaryKey(id, null), roleVo);
		return roleVo;
	}

	@Override
	@Transactional
	public boolean saveOrUpdateRole(RoleVo roleVo) throws IllegalAccessException, InvocationTargetException {
		Role role = new Role();
		BeanUtils.copyProperties(roleVo, role);
		Boolean flag = true;
		try {
			if (StrTool.isBlank(roleVo.getId())) {
				role.setEnableFlag(SuperConstant.YES);
				roleMapper.insert(role);
				roleVo.setId(role.getId());
			} else {
				roleMapper.updateByPrimaryKey(role);
				RoleResourceExample roleResourceExample = new RoleResourceExample();
				roleResourceExample.createCriteria().andRoleIdEqualTo(role.getId());
				roleResourceMapper.deleteByExample(roleResourceExample);
			}
			bachRoleResource(roleVo);
		} catch (Exception e) {
			log.error("保存角色出错{}", ExceptionsUtil.getStackTraceAsString(e));
			flag = false;
		}
		return flag;
	}

	/**
	 * 批量处理RoleResource中间表
	 */
	private void bachRoleResource(RoleVo roleVo) {
		if (StrTool.isNotBlank(roleVo.getHasResourceIds())) {
			List<RoleResource> list = new ArrayList<>();
			List<String> resourceIdList = Arrays.asList(roleVo.getHasResourceIds().split(","));
			for (String resourceId : resourceIdList) {
				RoleResource roleResource = new RoleResource();
				roleResource.setRoleId(roleVo.getId());
				roleResource.setResourceId(resourceId);
				roleResource.setEnableFlag(SuperConstant.YES);
				list.add(roleResource);
			}
			roleResourceMapper.batchInsert(list);
		}
	}

	@Override
	@Transactional
	public Boolean updateByIds(List<String> list, String enableFlag) {
		RoleExample roleExample = new RoleExample();
		roleExample.createCriteria().andIdIn(list);
		Role role = new Role();
		role.setEnableFlag(enableFlag);
		int row = roleMapper.updateByExampleSelective(role, roleExample);
		if (row > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Role findRoleByLable(String Label) {
		RoleExample roleExample = new RoleExample();
		if (StrTool.isNotBlank(Label)) {
			roleExample.createCriteria().andLabelEqualTo(Label);
		}
		List<Role> list = roleMapper.selectByExample(roleExample);
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ComboboxVo> findRoleComboboxVo(String roleIds) {
		RoleExample roleExample = new RoleExample();
		roleExample.createCriteria().andEnableFlagEqualTo(SuperConstant.YES);
		List<Role> roleList = roleMapper.selectByExample(roleExample);
		List<ComboboxVo> list = new ArrayList<>();
		for (Role role : roleList) {
			ComboboxVo comboboxVo = new ComboboxVo();
			comboboxVo.setId(role.getId());
			comboboxVo.setText(role.getRoleName());
			list.add(comboboxVo);
		}
		if (StrTool.isNotBlank(roleIds)) {
			String[] ids = roleIds.split(",");
			for (String id : ids) {
				for (ComboboxVo comboboxVo : list) {
					if (id.equals(comboboxVo.getId())) {
						comboboxVo.setSelected(true);
						break;
					}
				}
			}
		}
		return list;

	}

	@Override
	public List<String> findRoleHasResourceIds(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("enableFlag", SuperConstant.YES);
		map.put("id", id);
		return roleServiceMapper.findRoleHasResourceIds(map);
	}
}