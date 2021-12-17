package com.wy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wy.User;
import com.wy.base.Mapper;
import com.wy.enums.UserType;

/**
 * mybatis使用的对应user类的对应类,相当于UserDao,但是不能使用jpa,所有sql都需要自己写.该类使用的方法和UserDao一样
 * 
 * @Select 是查询类的注解，所有的查询均使用这个
 * @Result 修饰返回的结果集，关联实体类属性和数据库字段一一对应，如果实体类属性和数据库属性名保持一致，就不需要这个属性来修饰。
 * @Insert 插入数据库使用，直接传入实体类会自动解析属性到对应的值
 * @Update 负责修改，也可以直接传入对象
 * @delete 负责删除
 * 
 * 在sql语句中使用#代表该字段会被预编译成?,而使用$则是直接使用字符串或数据,会造成sql注入,最好都可以使用的时候使用#
 * 
 * @author wanyang 2018年7月30日
 */
public interface UserMapper extends Mapper<User> {

	@Select("select * from user")
	@Results({ @Result(property = "userType", column = "userType", javaType = UserType.class) })
	List<User> findAll();
	
	@Update("UPDATE users SET userName=#{userName},nick_name=#{nickName} WHERE id =#{id}")
	<T> void updateById(User user);
}