package dream.study.authorization.server.mapper;

import org.apache.ibatis.annotations.Mapper;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;
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
@Mapper
public interface ResourceMapper extends BaseMappers<ResourceEntity, ResourceVO, ResourceQuery> {

}