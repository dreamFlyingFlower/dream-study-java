//package com.wy.virtual;
//
//import java.util.concurrent.Executors;
//
//import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.AsyncTaskExecutor;
//import org.springframework.core.task.support.TaskExecutorAdapter;
//
///**
// * Java虚拟线程,从JDK19开始
// * 
// * @author 飞花梦影
// * @date 2024-05-28 14:22:16
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//@Configuration
//public class VirtualThreadConfig {
//
//	@Bean
//	AsyncTaskExecutor applicationTaskExecutor() {
//		return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
//	}
//
//	@Bean
//	TomcatProtocolHandlerCustomizer<?> protocolHandlerCustomizer() {
//		return protocolHandler -> {
//			protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
//		};
//	}
//}