package dream.study.authorization.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;
import dream.study.authorization.server.entity.RoleEntity;
import dream.study.authorization.server.entity.RoleMemberEntity;
import dream.study.authorization.server.query.RoleMemberQuery;
import dream.study.authorization.server.vo.RoleMemberVO;
import dream.study.authorization.server.vo.UserVO;

/**
 * 角色成员
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RoleMemberMapper extends BaseMappers<RoleMemberEntity, RoleMemberVO, RoleMemberQuery> {

	List<UserVO> memberInRole(RoleMemberQuery query);

	int addDynamicRoleMember(RoleEntity dynamicRole);

	int deleteDynamicRoleMember(RoleEntity dynamicRole);
}