package dream.study.authorization.server.service.impl;

import org.springframework.stereotype.Service;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.study.authorization.server.convert.RoleMemberConvert;
import dream.study.authorization.server.entity.RoleMemberEntity;
import dream.study.authorization.server.mapper.RoleMemberMapper;
import dream.study.authorization.server.query.RoleMemberQuery;
import dream.study.authorization.server.service.RoleMemberService;
import dream.study.authorization.server.vo.RoleMemberVO;
import lombok.AllArgsConstructor;

/**
 * 角色成员
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class RoleMemberServiceImpl extends
		AbstractServiceImpl<RoleMemberEntity, RoleMemberVO, RoleMemberQuery, RoleMemberConvert, RoleMemberMapper>
		implements RoleMemberService {
}