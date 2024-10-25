package dream.study.authorization.server.mapper;

import org.apache.ibatis.annotations.Mapper;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;
import dream.study.authorization.server.entity.UserEntity;
import dream.study.authorization.server.query.UserQuery;
import dream.study.authorization.server.vo.UserVO;

/**
 * 用户信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface UserMapper extends BaseMappers<UserEntity, UserVO, UserQuery> {

}