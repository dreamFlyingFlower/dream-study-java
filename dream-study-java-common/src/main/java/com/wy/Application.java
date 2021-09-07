package com.wy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 * 
 * <pre>
 * 配置Eclipse的运存:修改eclipse目录下的eclipse.ini,添加两行:-Xms1024m(最小运存),-Xmx2048m(最大运存)
 * 修改JDK运存:在eclipse->window->preferences->Java->Installed JRE->Edit->Default VM Arguments:-Xms512m -Xmx2014m -XX:MaxNewSize=256m -XX:MaxPermSize=256m
 * 修改单个项目运存:Run AS->Run Configuration->Arguments->VM arguments:-Xms1024m -Xmx2048m
 * Tomcat:修改start.sh或catalina.bat,添加:JAVA_OPTS='-Xms256m -Xmx512m -XX:MaxNewSize=256m -XX:MaxPermSize=256m'
 * </pre>
 * 
 * Druid的使用:ip:port/druid/index.html
 * 
 * 一些工具以及问题
 * 
 * @apiNote JavavisualVM:java虚拟机性能分析工具<br>
 *          jenkins:项目打包部署自动化<br>
 *          jmeter,loadrunner:压力测试工具<br>
 *          sonarqube:代码检测工具,findbugs检查代码中的bug
 *
 * @apiNote Kryo:更好的序列化框架<br>
 *          Jenkins:代码持续集成自动化部署<br>
 *          Gitlab-Cli:代码测试集成自动部署<br>
 *          apache-james:搭建私人的邮件服务器,Foxmail进行邮件测试
 * 
 * @apiNote java自带的一些小工具,在jdk安装目录的bin下:jps -l/-v,jstat等,比较有用的是以下2个
 *          jconsole:可视化查看当前虚拟机中基本的信息,例如CPI,堆,栈,类,线程信息
 *          jvisualvm:jconsole的更强版本,可视化工具,能看到JVM当前几乎所有运行程序的详细信息<br>
 *          javap:查看class文件的字节码信息
 * 
 * @apiNote CAP:一致性,可用性,分区容忍性,三个要素最多满足2个,不可能同时满足3个
 *          C:Consistency,在分布式系统中的所有数据备份,在同一时刻是否同样的值,如事务的强一致性
 *          A:Availability,在集群中部分节点故障后,集群整体是否还能响应客户端的请求<br>
 *          P:Partition tolerance,多节点必须在限定的时间内完成数据的一致性,若无法完成,就表示发生了分区,必须做出选择
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