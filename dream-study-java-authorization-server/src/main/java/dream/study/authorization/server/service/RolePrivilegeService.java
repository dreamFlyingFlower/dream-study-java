package dream.study.authorization.server.service;

import java.util.List;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;
import dream.study.authorization.server.entity.RolePrivilegeEntity;
import dream.study.authorization.server.query.RolePrivilegeQuery;
import dream.study.authorization.server.vo.RolePrivilegeVO;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface RolePrivilegeService extends BaseServices<RolePrivilegeEntity, RolePrivilegeVO, RolePrivilegeQuery> {

	boolean insertRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList);

	boolean deleteRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList);

	List<RolePrivilegeEntity> queryRolePrivileges(RolePrivilegeEntity rolePermissions);
}