package dream.study.authorization.server.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.study.authorization.server.convert.RolePrivilegeConvert;
import dream.study.authorization.server.entity.RolePrivilegeEntity;
import dream.study.authorization.server.mapper.RolePrivilegeMapper;
import dream.study.authorization.server.query.RolePrivilegeQuery;
import dream.study.authorization.server.service.RolePrivilegeService;
import dream.study.authorization.server.vo.RolePrivilegeVO;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class RolePrivilegeServiceImpl extends AbstractServiceImpl<RolePrivilegeEntity, RolePrivilegeVO,
		RolePrivilegeQuery, RolePrivilegeConvert, RolePrivilegeMapper> implements RolePrivilegeService {

	@Override
	public boolean insertRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList) {
		return baseMapper.insertRolePrivileges(rolePermissionsList) > 0;
	}

	@Override
	public boolean deleteRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList) {
		return baseMapper.deleteRolePrivileges(rolePermissionsList) >= 0;
	}

	@Override
	public List<RolePrivilegeEntity> queryRolePrivileges(RolePrivilegeEntity rolePermissions) {
		return baseMapper.queryRolePrivileges(rolePermissions);
	}
}