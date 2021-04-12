package com.wy.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wy.base.BaseRepository;
import com.wy.model.User;

/**
 * JpaUserRepository自定义方法实现类,通用方法已经写在了JpaRepository中,接口中只需要写特殊方法即可
 * 
 * @author 飞花梦影
 * @date 2018-07-16 19:50:26
 * @git {@link https://github.com/mygodness100}
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long> {

	@Query(nativeQuery = true, value = "select * from ti_user where username=:username")
	User findByUsername(@Param("username") String username);
}