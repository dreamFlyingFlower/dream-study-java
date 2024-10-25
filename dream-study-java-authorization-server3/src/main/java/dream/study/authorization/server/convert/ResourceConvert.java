package dream.study.authorization.server.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import dream.flying.flower.framework.web.convert.BaseConvert;
import dream.study.authorization.server.entity.ResourceEntity;
import dream.study.authorization.server.vo.ResourceVO;

/**
 * 资源
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceConvert extends BaseConvert<ResourceEntity, ResourceVO> {

	ResourceConvert INSTANCE = Mappers.getMapper(ResourceConvert.class);
}