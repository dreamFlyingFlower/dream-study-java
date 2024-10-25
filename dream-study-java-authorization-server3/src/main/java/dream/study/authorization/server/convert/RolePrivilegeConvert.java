package dream.study.authorization.server.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import dream.flying.flower.framework.web.convert.BaseConvert;
import dream.study.authorization.server.entity.RolePrivilegeEntity;
import dream.study.authorization.server.vo.RolePrivilegeVO;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolePrivilegeConvert extends BaseConvert<RolePrivilegeEntity, RolePrivilegeVO> {

	RolePrivilegeConvert INSTANCE = Mappers.getMapper(RolePrivilegeConvert.class);
}