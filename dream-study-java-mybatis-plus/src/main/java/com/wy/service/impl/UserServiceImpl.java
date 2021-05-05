package com.wy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.base.AbstractService;
import com.wy.mapper.UserMapper;
import com.wy.model.User;
import com.wy.service.UserService;

/**
 * User用户业务类
 * 
 * @author 飞花梦影
 * @date 2021-04-08 13:56:03
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class UserServiceImpl extends AbstractService<User, Long> implements UserService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 分页,需要先配置{@link PaginationInnerInterceptor}
	 */
	public void getPage(User user) {
		QueryWrapper<User> query = new QueryWrapper<User>();
		query.eq("user_id", user.getUserId());
		// 相当于MyBatis中的if标签,若前面的条件成立,才使用后面的查询条件
		query.eq("test".equals(user.getUsername()), "user_id", user.getUserId());
		// 排序
		query.orderByAsc("user_id");
		// 当前页,小于1按1算;每页记录数
		Page<User> page = new Page<>(1, 1);
		Page<User> selectPage = userMapper.selectPage(page, query);
		// 分页数据
		selectPage.getRecords();
	}

	@Override
	public Object resetPwd(User user) {
		User detail = baseMapper.selectById(user.getUserId());
		System.out.println(user.getPassword());
		System.out.println(detail.getPassword());
		return null;
	}
}