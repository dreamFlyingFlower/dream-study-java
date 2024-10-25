package dream.study.authorization.server.service;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;
import dream.study.authorization.server.entity.RoleMemberEntity;
import dream.study.authorization.server.query.RoleMemberQuery;
import dream.study.authorization.server.vo.RoleMemberVO;

/**
 * 角色成员
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface RoleMemberService extends BaseServices<RoleMemberEntity, RoleMemberVO, RoleMemberQuery> {
}