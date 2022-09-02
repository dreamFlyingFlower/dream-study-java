package com.wy.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.wy.model.User;

import reactor.core.publisher.Flux;

/**
 * User的Reactive数据操作层
 *
 * @author 飞花梦影
 * @date 2022-09-02 10:20:56
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

	/**
	 * 根据年龄上下限查询
	 * 
	 * @param below 年龄下限(不包含此边界点)
	 * @param top 年龄上限(不包含此边界点)
	 * @return
	 */
	Flux<User> findByAgeBetween(int below, int top);

	/**
	 * 使用MongoDB原始查询实现根据年龄上下限查询
	 * 
	 * @param below
	 * @param top
	 * @return
	 */
	// {age:{$gte:21, $lt:25}}
	@Query("{'age':{'$gte':?0, '$lt':?1}}")
	Flux<User> queryByAge(int below, int top);
}