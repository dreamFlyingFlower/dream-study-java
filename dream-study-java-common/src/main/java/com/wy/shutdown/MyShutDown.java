package com.wy.shutdown;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 不使用kill -9强制停机,使用其他方式优雅下线
 * 
 * <pre>
 * 1.使用SpringBoot Actuator,默认情况下,Actuator的shutdown是disable的,需要在配置文件中打开
 *		management.endpoint.shutdown.enabled=true
 *		management.endpoints.web.exposure.include=shutdown
 *		若想要关闭,可通过ip:port/actuator/shutdown调用接口关闭,需要post方式
 *	2.通常启动项目时会返回一个{@link ConfigurableApplicationContext},调用该接口的close()进行关闭
 * 3.SpringBoot启动时将进程号写入一个app.pid文件,可指定生成路径,通过命令 cat /app/app.id | xargs kill 命令直接停止服务.
 * 		这种方法使用的比较普遍,写一个start.sh用于启动SpringBoot程序,然后写一个停止程序将服务停止
 * 4.调用SpringApplication.exit(),退出同时将生成一个退出码,这个退出码可以传递给所有的context.
 * 		通过调用System.exit(exitCode)可以将这个错误码也传给JVM,程序执行完后会输出:Process finished with exit code 0
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-12-07 11:35:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyShutDown {

	public void showdown2() {
		ConfigurableApplicationContext ctx = SpringApplication.run(MyShutDown.class);
		ctx.close();
	}

	public void showdown3() {
		SpringApplication application = new SpringApplication(MyShutDown.class);
		application.addListeners(new ApplicationPidFileWriter("/app/app.pid"));
		application.run();
	}

	public void showdown4() {
		ConfigurableApplicationContext ctx = SpringApplication.run(MyShutDown.class);
		int exitCode = SpringApplication.exit(ctx, (ExitCodeGenerator) () -> 0);
		System.exit(exitCode);
	}
}