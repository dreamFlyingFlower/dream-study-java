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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wy.lang.StrTool;
import com.wy.shiro.constant.SuperConstant;
import com.wy.shiro.entity.Role;
import com.wy.shiro.entity.RoleResource;
import com.wy.shiro.entity.vo.ComboboxVo;
import com.wy.shiro.entity.vo.RoleVo;
import com.wy.shiro.mapper.RoleMapper;
import com.wy.shiro.mapper.RoleResourceMapper;
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
public class RoleServiceImp extends ServiceImpl<RoleMapper, Role> implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private RoleResourceMapper roleResourceMapper;

	@Override
	public List<Role> findRoleList(RoleVo roleVo, Integer rows, Integer pageIndex) {
		LambdaQueryChainWrapper<Role> chainWrapper = generateCondition(roleVo);
		Page<Role> page = chainWrapper.orderByAsc(Role::getSortNo).page(new Page<Role>(pageIndex, rows));
		return page.getRecords();
	}

	@Override
	public long countRoleList(RoleVo roleVo) {
		LambdaQueryChainWrapper<Role> chainWrapper = generateCondition(roleVo);
		return count(chainWrapper);
	}

	private LambdaQueryChainWrapper<Role> generateCondition(RoleVo roleVo) {
		return this.lambdaQuery()
		        .like(StrTool.isNotBlank(roleVo.getRoleName()), Role::getRoleName, roleVo.getRoleName())
		        .eq(StrTool.isNotBlank(roleVo.getLabel()), Role::getLabel, roleVo.getLabel());
	}

	@Override
	public RoleVo getRoleById(String id) {
		RoleVo roleVo = new RoleVo();
		BeanUtils.copyProperties(getById(id), roleVo);
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
				roleMapper.updateById(role);
				roleResourceMapper
				        .delete(new QueryWrapper<RoleResource>().lambda().eq(RoleResource::getRoleId, role.getId()));
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
		return this.lambdaUpdate().set(Role::getEnableFlag, enableFlag).in(Role::getId, list).update();
	}

	@Override
	public Role findRoleByLable(String label) {
		List<Role> list = this.lambdaQuery().eq(StrTool.isNotBlank(label), Role::getLabel, label).list();
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ComboboxVo> findRoleComboboxVo(String roleIds) {
		List<Role> roleList = this.lambdaQuery().eq(Role::getEnableFlag, SuperConstant.YES).list();
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
		return roleMapper.findRoleHasResourceIds(map);
	}
}