package com.wy.jdbc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

import com.wy.entity.TestData;

/**
 * JdbcClient,可使用复杂的SQL语句,但不能批量操作和存储过程
 *
 * @author 飞花梦影
 * @date 2023-12-22 10:33:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyJdbcClient {

	@Autowired
	private JdbcClient jdbcClient;

	public void test1() {
		// 使用占位符取单个值
		TestData testData =
				jdbcClient.sql("select * from my_data where id = ?").params("id的值").query(TestData.class).single();
		System.out.println(testData);

		// 使用占位符取多个值
		List<TestData> testDatas = jdbcClient.sql("select * from my_data where username = ?").params("username的值")
				.query(TestData.class).list();
		System.out.println(testDatas);

		// 按参数名传参
		List<TestData> testDatas1 = jdbcClient.sql("select * from my_data where username = :username")
				.params("username", "username的值").query(TestData.class).list();
		System.out.println(testDatas1);

		// 多参数使用Map进行传参
		Map<String, Object> params1 = new HashMap<>();
		params1.put("username", "username");
		List<TestData> testDatas2 = jdbcClient.sql("select * from my_data where username = :username").params(params1)
				.query(TestData.class).list();
		System.out.println(testDatas2);

		// 可只从查询结果中取某些值构建结果,适用于联表查询
		jdbcClient.sql("select * from my_data").query(
				(rs, rowNum) -> new TestData(rs.getLong("id"), rs.getString("username"), rs.getBigDecimal("salary")))
				.list();

		// 查询记录数
		jdbcClient.sql("select count(*) from my_data where username = ?").params("username的值").query(Integer.class)
				.single();

		// 使用占位符插入数据
		jdbcClient.sql("insert into my_data values(?,?) ").param("id的值").param("username的值").update();

		// 按参数名新增
		jdbcClient.sql("insert into my_data values(:id,:username) ").param("id", "id的值").param("username", "username的值")
				.update();

		// 直接插入整个值
		jdbcClient.sql("insert into my_data values(:id,:username,:salary) ")
				.paramSource(new TestData(1L, "username", BigDecimal.ZERO)).update();

		// 更新
		jdbcClient.sql("update my_data set username = ? where id = ?").param("username的值", "id的值").update();
	}
}