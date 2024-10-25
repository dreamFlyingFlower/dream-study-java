package dream.study.authorization.server.service;

import java.util.List;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;
import dream.study.authorization.server.entity.ResourceEntity;
import dream.study.authorization.server.query.ResourceQuery;
import dream.study.authorization.server.vo.ResourceVO;

/**
 * 资源
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ResourceService extends BaseServices<ResourceEntity, ResourceVO, ResourceQuery> {

	List<ResourceEntity> queryResourcesTree(ResourceEntity resource);
}