package com.wy.mybatis;

/**
 * mybatis不整合到spring时使用方法
 * 
 * @author ParadiseWY
 * @date 2020-09-28 14:48:23
 */
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.wy.model.User;

@SpringBootTest
public class TestMybatis {

	/**
	 * 此种测试方式并没有结合spring,但是对理解mybatis源码有帮助
	 */
	@Test
	public void testBaseDao() {
		String globalConfig = "mybatis.xml";
		try {
			InputStream is = Resources.getResourceAsStream(globalConfig);
			SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
			SqlSession session = factory.openSession(true);// true表示自动提交
			User user = session.selectOne("com.wy.mapper.UserMapper.getById", 1);
			System.out.println(user);
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}