package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;

/**
 * Jpa,自动配置{@link JpaRepositoriesAutoConfiguration}
 * 
 * 底层其实是{@link SimpleJpaRepository}对接口的代理,而被代理的接口必须继承{@link Repository}
 * 
 * @author 飞花梦影
 * @date 2020-09-25 23:11:18
 * @git {@link https://github.com/mygodness100}
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}