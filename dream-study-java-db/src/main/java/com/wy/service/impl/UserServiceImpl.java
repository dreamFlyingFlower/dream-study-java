package com.wy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wy.mapper.UserMapper;
import com.wy.model.User;
import com.wy.service.UserService;

/**
 * Cacheable对应的是ecache的缓存,有默认配置,也可以进行自定义配置
 * 
 * 在配置了rediscache之后,在方法上可以使用注解将返回值直接缓存到redis中,value的值就是key
 * 
 * CacheEvict:清除缓存,特别是新增和更新的时候需要清除缓存
 * 
 * @author ParadiseWY
 * @date 2020年9月26日 下午1:10:53
 */
@CacheConfig(cacheNames = "user")
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 防止重复提交,可使用redis的分布式锁,zk的分布式锁,数据库的悲观锁,乐观锁.<br>
	 * 乐观锁:当更新数据库的某一条信息成功时,数据库返回1,此时可拿到锁.更新失败,则表示没有拿到锁.<br>
	 * 更新时必须是唯一一条数据,并且更新时候确实改变了某一条数据的值,否则锁失效
	 */
	@Transactional
	public void repeatSubmit(User user) {
		// 更新user的状态为2,此处若是重复提交,第一次更新成功之后,状态变为2,重复提交的数据更新则会失败
		userMapper.updateById(User.builder().userId(user.getUserId()).state(1).build());
	}
}