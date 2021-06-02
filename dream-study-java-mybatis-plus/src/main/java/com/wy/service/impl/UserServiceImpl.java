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
 * Mybatis-plus的逻辑删除:
 *
 * <pre>
 * 全局配置:
 * 全局逻辑删除的实体字段名,即所有用到逻辑删除的表中的字段对应的Java属性名
 * mybatis-plus.global-config.db-config.logic-delete-field = deleted
 * 逻辑已删除值(默认为1),若使用默认值,可不配置
 * mybatis-plus.global-config.db-config.logic-delete-value = 1
 * 逻辑未删除值(默认为0),若使用默认值,可不配置
 * mybatis-plus.global-config.db-config.logic-not-delete-value = 0
 * 若不适用全局配置的逻辑删除值,可使用@TableLogic自定义删除值
 *
 * 当配置了逻辑删除知乎,Mybatis-plus中预定义的SQL都会有所改变,但是自定义的SQL不会有影响,需要自行过滤:
 * INSERT:无影响
 * SELECT:追加where过滤条件,过滤已删除数据
 * UPDATE:追加WHERE过滤条件,避免更新已删除数据
 * DELETE:转变为UPDATE语句,只更新逻辑删除的字段
 *
 * 若查询时不需要将逻辑删除字段查出,可以在逻辑删除字段上的设置@TableField(select=false)
 * </pre>
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
		// 只查询指定列,注意是数据库字段
		query.select("user_id","username");
		// 字段比较多时,排除某些列
		query.select(t -> {
			return t.getColumn() != "username";
		});
		// 相等条件
		query.eq("user_id", user.getUserId());
		// 相当于MyBatis中的if标签,若前面的条件成立,才使用后面的查询条件
		query.eq("test".equals(user.getUsername()), "user_id", user.getUserId());
		// 相当于(username='admin' or mobile='admin') and username='test'
		query.nested(t->t.eq("username", "admin").or().eq("mobile", "admin")).eq("username", "test");
		// 参数拼接,常用于使用函数
		query.apply("date_format(createtime, '%Y-%m-%d') = {0}", "2021-03-22");
		// 同上,但是会有SQL注入风险
		query.apply("date_format(create_time, '%Y-%m-%d') = '2021-03-22'");
		// 子查询
		query.inSql("user_id", "SELECT id FROM user WHERE name like 'admin%'");
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