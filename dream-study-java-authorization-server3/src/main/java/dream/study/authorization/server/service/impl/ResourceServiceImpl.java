package dream.study.authorization.server.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.study.authorization.server.convert.ResourceConvert;
import dream.study.authorization.server.entity.ResourceEntity;
import dream.study.authorization.server.mapper.ResourceMapper;
import dream.study.authorization.server.query.ResourceQuery;
import dream.study.authorization.server.service.ResourceService;
import dream.study.authorization.server.vo.ResourceVO;

/**
 * 资源
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class ResourceServiceImpl
		extends AbstractServiceImpl<ResourceEntity, ResourceVO, ResourceQuery, ResourceConvert, ResourceMapper>
		implements ResourceService {

	@Override
	public List<ResourceEntity> queryResourcesTree(ResourceEntity resource) {
		return baseConvert.convert(list(resource));
	}
}