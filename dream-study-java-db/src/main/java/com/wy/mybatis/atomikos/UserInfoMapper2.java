package com.wy.mybatis.atomikos;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.wy.model.User;

/**
 * 跨数据库事务,只能在同类型数据库中使用
 *
 * @author 飞花梦影
 * @date 2024-04-22 17:05:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface UserInfoMapper2 {

	@Select("SELECT * FROM user_info WHERE username = #{username}")
	User findByName(@Param("username") String username);

	@Insert("INSERT INTO user_info(user_id,username, age) VALUES(#{userId},#{username}, #{age})")
	int insert(@Param("userId") String userId, @Param("username") String username, @Param("age") Integer age);
}