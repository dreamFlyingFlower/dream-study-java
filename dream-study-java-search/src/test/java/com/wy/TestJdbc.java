package com.wy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * es sql功能
 * 
 * @author 飞花梦影
 * @date 2022-08-05 16:30:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class TestJdbc {

	public static void main(String[] args) {
		try {
			// 创建连接
			Connection connection = DriverManager.getConnection("jdbc:es://http://localhost:9200");
			// 创建statement
			Statement statement = connection.createStatement();
			// 执行sql语句
			ResultSet resultSet = statement.executeQuery("select * from tvs");
			// 4获取结果
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1));
				System.out.println(resultSet.getString(2));
				System.out.println(resultSet.getString(3));
				System.out.println(resultSet.getString(4));
				System.out.println("======================================");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}