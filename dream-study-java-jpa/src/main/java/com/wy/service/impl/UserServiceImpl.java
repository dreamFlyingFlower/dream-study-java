package com.wy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wy.base.AbstractService;
import com.wy.model.User;
import com.wy.model.Userinfo;
import com.wy.repository.UserRepository;
import com.wy.service.UserService;

/**
 * JPA复杂查询
 *
 * @author 飞花梦影
 * @date 2022-12-29 10:17:30
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class UserServiceImpl extends AbstractService<User, Long> implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager entityManager;

	/**
	 * Jpa实现原理
	 */
	public void principle() {
		JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
		// 可以帮助接口生成实现类,而这个实现类是SimpleJpaRepository的动态代理,而该接口必须继承Repository
		UserRepository userRepository2 = factory.getRepository(UserRepository.class);
		System.out.println(userRepository2);
	}

	/**
	 * 添加用户
	 * 
	 * @return 结果
	 */
	@Transactional
	public User addUser() {
		User user = User.builder().username("飞花梦影").password("123456").sex("f").age(20).birthday(new Date())
				.realname("那一夜,雪舞").build();
		return userRepository.save(user);
	}

	/**
	 * 更新数据,需要先查询,查了修改数据之后,再次保存
	 * 
	 * 或者不保存,但是方法上必须添加事务
	 */
	@Transactional
	public void Update() {
		User user = userRepository.getOne(1l);
		user.setAge(33);
		// 不保存,但是必须添加@Transactional事务;保存可以不使用事务,但实际开发中必然会使用事务
		userRepository.save(user);
	}

	/**
	 * 防止重复提交,可使用redis的分布式锁,zk的分布式锁,数据库的悲观锁,乐观锁.<br>
	 * 乐观锁:当更新数据库的某一条信息成功时,数据库返回1,此时可拿到锁.更新失败,则表示没有拿到锁.<br>
	 * 更新时必须是唯一一条数据,并且更新时候确实改变了某一条数据的值,否则锁失效
	 */
	@Transactional
	public void repeatSubmit(User user) {
		// 更新user的状态为2,此处若是重复提交,第一次更新成功之后,状态变为2,重复提交的数据更新则会失败
		userRepository.saveAndFlush(User.builder().userId(user.getUserId()).state(1).build());
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 */
	public void getAll(Pageable page) {
		// 排序:排序类型,排序字段(可以写多个)
		Order order = new Order(Direction.DESC, "userId");
		// 分页排序
		PageRequest.of(1, 10, Sort.by(order));
		PageRequest of = PageRequest.of(1, 10, Direction.DESC, "userId");
		userRepository.findAll(of);
	}

	/**
	 * 使用example查询
	 * 
	 * {@link SimpleJpaRepository#findAll(Example)}->{@link SimpleJpaRepository#getQuery}
	 * ->{@link SimpleJpaRepository#applySpecificationToCriteria}
	 * ->{@link SimpleJpaRepository.ExampleSpecification#toPredicate}
	 * ->{@link QueryByExamplePredicateBuilder#getPredicate(Root, CriteriaBuilder, Example)}
	 * ->{@link QueryByExamplePredicateBuilder#getPredicates}
	 * 
	 * @param user
	 * @param page
	 */
	public void getAll1(User user, Pageable page) {
		// 忽略null,默认所有值以相等条件查询,也不能使用<,>之类的条件
		// 字符串可以有多重匹配模式,但其他类型只能使用equals比较
		Example.of(user);
		// 默认全匹配,精准匹配
		Example.of(user, ExampleMatcher.matchingAll()); // 等用于Example.of(user);
		ExampleMatcher.matchingAll();// 等同于ExampleMatcher.matching();
		// ==========
		// or,所有参数以or连接,而非and连接
		Example.of(user, ExampleMatcher.matchingAny());
		// ==========
		// 字符串其他匹配
		ExampleMatcher matching = ExampleMatcher.matching();
		matching = matching
				// 设置搜索的字段,字段必须与数据库相同,而非java属性,包含值为实体类中的值
				.withMatcher("数据库字段", ExampleMatcher.GenericPropertyMatchers.contains())
				// 忽略所有值的大小写
				.withIgnoreCase()
				// 忽略指定属性的大小写
				.withIgnoreCase("数据库字段")
				// 忽略某个字段的值,通常用于基本类型,因为基本类型会赋初始值
				.withIgnorePaths("")
				// 修改字符串默认匹配模式,默认为精准匹配,此处可改为模糊匹配
				.withStringMatcher(StringMatcher.CONTAINING)
				// 数据库值以实体参数中的值开头
				.withMatcher("数据库字段", GenericPropertyMatchers.startsWith());
		// 最后用这个构造获取Example
		Example<User> example = Example.of(user, matching);
		// 查询
		List<User> list = userRepository.findAll(example);
		System.out.println(list);
	}

	@Override
	public void getUserBySpecification() {
		// 用户封装查询条件
		Specification<User> spec = new Specification<User>() {

			private static final long serialVersionUID = 1L;

			/**
			 * Predicate:封装了单个查询条件<br>
			 * root:查询对象属性的封装,此处相当于User的封装类<br>
			 * query:封装了要执行的查询中的各个部分的信息,如select,from,order<br>
			 * criteriaBuilder:查询条件的构造器,用来定义不同的查询条件
			 */
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				// 混合条件查询
				Path<String> exp1 = root.get("username");
				Path<String> exp2 = root.get("phone");
				Path<Date> exp3 = root.get("createTime");
				Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(exp1, "%username%"),
						criteriaBuilder.lessThan(exp3, new Date()));
				criteriaBuilder.or(predicate, criteriaBuilder.equal(exp2, "13000000000"));
				// select count(user0_.id) as col_0_0_ from ti_user user0_ where
				// (user0_.username like ? and user0_.create_time<?) or user0_.phone=?

				// 多表查询
				Join<User, Userinfo> join = root.join("userinfo", JoinType.INNER);
				Path<String> exp4 = join.get("username");
				criteriaBuilder.like(exp4, "%username%");

				// select count(user0_.id) as col_0_0_ from ti_user user0_ inner join
				// ti_userinfo userinfo1_ on user0_.user_id=userinfo1_.user_id where
				// user0_.username like ?

				Predicate predicate1 = criteriaBuilder.equal(root.get("username"), "admin");
				Predicate predicate2 = criteriaBuilder.equal(root.get("password"), "admin");
				List<Predicate> list = new ArrayList<>();
				list.add(predicate1);
				list.add(predicate2);

				return criteriaBuilder.and(list.toArray(new Predicate[2]));
			}
		};
		userRepository.findAll(spec);
	}
}