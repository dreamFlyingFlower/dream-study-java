package dream.study.authorization.server.service.impl;

import org.springframework.stereotype.Service;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.study.authorization.server.convert.RoleConvert;
import dream.study.authorization.server.entity.RoleEntity;
import dream.study.authorization.server.mapper.RoleMapper;
import dream.study.authorization.server.query.RoleQuery;
import dream.study.authorization.server.service.RoleService;
import dream.study.authorization.server.vo.RoleVO;
import lombok.AllArgsConstructor;

/**
 * 角色
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl extends AbstractServiceImpl<RoleEntity, RoleVO, RoleQuery, RoleConvert, RoleMapper>
		implements RoleService {
}