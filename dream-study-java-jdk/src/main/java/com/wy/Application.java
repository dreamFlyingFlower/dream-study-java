package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 * 
 * <pre>
 * 修改Eclipse运存:eclipse目录下的eclipse.ini,添加两行:-Xms1024m(最小运存),-Xmx2048m(最大运存)
 * 修改JDK运存:preferences->Java->Installed JRE->Edit->Default VM Arguments:-Xms512m -Xmx2014m
 * 修改单个项目运存:Run AS->Run Configuration->Arguments->VM arguments:-Xms1024m -Xmx2048m
 * Tomcat:修改start.sh或catalina.bat,添加:JAVA_OPTS='-Xms256m -Xmx512m -XX:MaxNewSize=256m -XX:MaxPermSize=256m'
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-03-30 11:00:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class Application {

	/**
	 * 启动类
	 * 
	 * run():扫描springboot的上下文环境,装载自动配置类
	 * 
	 * @param args 当多环境启动时,可以加载不同的参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public static void free() {
		Runtime runtime = Runtime.getRuntime();
		// 可用内存,单位字节,换算成m需要除以1024的平方
		System.out.println(runtime.freeMemory());
		// 总内存字节,单位字节,换算成m需要除以1024的平方
		System.out.println(runtime.totalMemory());
	}
}