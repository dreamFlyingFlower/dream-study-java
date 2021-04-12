package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 爬虫,使用webmagic,selenium
 * 
 * 下载完成数据后就需要进行文档处理,这里的处理是分三个步骤<br>
 * 1.解析初始化的URL获取列表页,将列表页的数据提交下载处理器<br>
 * 2.解析完列表页后获取最终的需要处理的URL交给下载处理器<br>
 * 3.解析最终URL数据,将解析的数据交给下一级处理器处理<br>
 * 
 * @author 飞花梦影
 * @date 2021-01-07 14:08:13
 * @git {@link https://github.com/mygodness100}
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}