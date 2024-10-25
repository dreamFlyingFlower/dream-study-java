package dream.study.authorization.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;
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
@Mapper
public interface RolePrivilegeMapper extends BaseMappers<RolePrivilegeEntity, RolePrivilegeVO, RolePrivilegeQuery> {

	int insertRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList);

	int deleteRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList);

	List<RolePrivilegeEntity> queryRolePrivileges(RolePrivilegeEntity rolePermissions);
}