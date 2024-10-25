package dream.study.authorization.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;
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
@Mapper
public interface RoleMapper extends BaseMappers<RoleEntity, RoleVO, RoleQuery> {

	List<RoleEntity> queryRolesByUserId(String userId);
}