package dream.study.authorization.server.service;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;
import dream.study.authorization.server.entity.RoleEntity;
import dream.study.authorization.server.query.RoleQuery;
import dream.study.authorization.server.vo.RoleVO;

/**
 * 角色
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface RoleService extends BaseServices<RoleEntity, RoleVO, RoleQuery> {
}