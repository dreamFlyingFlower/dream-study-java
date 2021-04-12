package com.wy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.wy.mapper.UserMapper;
import com.wy.model.User;
import com.wy.service.UserService;

/**
 * Cacheable对应的是ecache的缓存,有默认配置,也可以进行自定义配置
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
	 * 在配置了rediscache之后,在方法上可以使用注解将返回值直接缓存到redis中,value的值就是key CacheEvict:清除缓存,特别是新增和更新的时候需要清除缓存
	 */
	@Autowired
	private TransactionTemplate transactionTemplate;

	/**
	 * 当需要进行事务的操作中有比较长时间的API调用时,开启事务将会一直占用数据库的连接数,
	 * 在高并发时将会造成数据库宕机.此时可使用TransactionTemplate对部分需要进行数据库操作的代码块进行事务操作
	 * 该方法可能会在其他含有事务操作的方法中调用,若外层方法中仍使用事务,则本方法仍有可能会使用事务<br>
	 * 此处方法的Transactional注解应使用不可使用事务类型
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void handlerOverTime() {
		// 假设此处的RPC调用可能需要20多秒完成,高并发可能会造成数据库宕机
		transactionTemplate.execute((param) -> {
			// 需要进行事务的操作
			return null;
		});
	}

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