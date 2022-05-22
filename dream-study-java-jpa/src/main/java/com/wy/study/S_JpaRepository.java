package com.wy.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wy.model.User;

/**
 * Jpa适合简单的数据库操作,复杂操作仍需要用sql实现,spring整个jpa之后,数据层可直接继承{@link JpaRepository}<br>
 * Jpa在这里遵循Convention over configuration(约定大于配置)的原则,遵循spring以及JPQL定义的方法命名.
 * {@link JpaSpecificationExecutor}:同JpaRepository,但是是对复杂的sql进行操作,不能单独继承该类
 * 
 * Spring提供了一套可以通过命名规则进行查询构建的机制,这套机制会把方法名首先过滤一些关键字,比如:
 * ->find…By,read…By,query…By,count…By和get…By,系统会根据关键字将命名解析成2个子语句:
 * ->->By是区分这两个子语句的关键词.By之前的子语句是查询子语句(指明返回要查询的对象),后面的部分是条件子语句.<br>
 * 如果直接就是findBy…,返回的就是定义Respository时指定的实体类对象集合
 * 
 * JPQL中也定义了丰富的关键字:and,or,Between等等,如:<br>
 * And:findByLastnameAndFirstname -> where x.lastname = ?1 and x.firstname =
 * ?2<br>
 * Or:findByLastnameOrFirstname -> where x.lastname = ?1 or x.firstname = ?2<br>
 * Is,Equals:findByFirstnameIs,findByFirstnameEquals -> where x.firstname =
 * ?1<br>
 * Between:findByStartDateBetween -> where x.startDate between ?1 and ?2<br>
 * LessThan:findByAgeLessThan -> where x.age < ?1<br>
 * LessThanEqual:findByAgeLessThanEqual -> where x.age <= ?1<br>
 * GreaterThan:findByAgeGreaterThan -> where x.age > ?1<br>
 * GreaterThanEqual:findByAgeGreaterThanEqual -> where x.age >= ?1<br>
 * After:findByStartDateAfter -> where x.startDate > ?1<br>
 * Before:findByStartDateBefore -> where x.startDate < ?1<br>
 * IsNull:findByAgeIsNull -> where x.age is null<br>
 * IsNotNull,NotNull:findByAge(Is)NotNull -> where x.age is not null<br>
 * Like:findByFirstnameLike -> where x.firstname like ?1<br>
 * NotLike:findByFirstnameNotLike -> where x.firstname not like ?1<br>
 * StartingWith:findByFirstnameStartingWith -> where x.firstname like
 * ?1;%会加在参数结尾<br>
 * EndingWith:findByFirstnameEndingWith -> where x.firstname like
 * ?1;%会加在参数前面<br>
 * Containing:findByFirstnameContaining -> where x.firstname like
 * ?1;参数前后都会加上%<br>
 * OrderBy:findByAgeOrderByLastnameDesc -> where x.age = ?1 order by x.lastname
 * desc<br>
 * Not:findByLastnameNot -> where x.lastname <> ?1<br>
 * In:findByAgeIn(Collection ages) -> where x.age in ?1<br>
 * NotIn:findByAgeNotIn(Collection age) -> where x.age not in ?1<br>
 * TRUE:findByActiveTrue() -> where x.active = true<br>
 * FALSE:findByActiveFalse() -> where x.active = false<br>
 * IgnoreCase:findByFirstnameIgnoreCase -> where UPPER(x.firstame) =
 * UPPER(?1)<br>
 * 
 * {@link JpaRepository}:该接口的泛型参数,第一个是实体类,第二个是实体类中的主键类型
 * 
 * @author 飞花梦影
 * @date 2018-07-16 19:46:55
 * @git {@link https://github.com/mygodness100}
 */
@Repository
public interface S_JpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	/**
	 * 通过userId查询用户信息,可以没有任何注解
	 * 
	 * 方法的名称必须遵循小驼峰命名规则,findBy(关键字)+属性名称(首字母大写)+查询条件(首字母大写)
	 * 
	 * @param userId
	 * @return
	 */
	User findByUserId(@Param("userId") Long userId);

	/**
	 * 同findByUserId,默认是相等的条件
	 * 
	 * @param userId
	 * @return
	 */
	User findByUserIdEquals(@Param("userId") Long userId);

	/**
	 * Query注解表示自定义sql语句,不使用JPQL语法,nativeQuery为true表示原生sql原句,默认false表示jpql语句
	 * 
	 * 多参数可以使用Param注解中定义的值对应,也可以对应参数位置,使用?占位符
	 * 
	 * @param username
	 * @return
	 */
	// @Query(nativeQuery = true, value = "select * from ti_user where
	// username=:username and age
	// >:age")
	@Query(nativeQuery = true, value = "select * from ti_user where username=? and age > ?")
	User findByUsername(@Param("username") String username, int age);

	/**
	 * Query注解表示默认使用jpql语言,参数可以使用?加上数字,数字表示参数的位置,其中的表可以使用实体类名代替
	 * 
	 * @param username
	 * @return
	 */
	@Query("from User where username=?1")
	User findByUsernameLike(@Param("username") String username);

	/**
	 * 多参数时,若在?后不指定数字,默认参数从左至右和jpql语句中的占位符一一对应
	 * 
	 * @param userId
	 * @param age
	 * @return
	 */
	@Query("from User where username=? and age > ?")
	User findByUsernameById(@Param("userId") Long userId, Integer age);

	/**
	 * 参数为实体类,可使用以下方式拼接参数.
	 * 
	 * 其中的entityName是jpa与实体类绑定的@Entity注解中的name的值,若不定义,则是实体类类名
	 * 
	 * @param user 实体参数
	 * @return
	 */
	@Query("from #{#entityName} where userId=#{#user.userId}")
	User findById(User user);

	/**
	 * 对结果进行分页查询,若有多个参数,pageable放最后,该类型的实现类为{@link PageRequest}
	 */
	@Override
	Page<User> findAll(Pageable page);

	Page<User> findByUsername(String username, Pageable page);

	/**
	 * 若需要新增(insert),修改(update)或删除(delete),必须使用@Modifying并开启事务
	 */
	@Transactional
	@Modifying
	int deleteByUserId(int id);
}