package com.wy.netty.tcp.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Netty TCP长连接服务端启动类
 *
 * @author 飞花梦影
 * @date 2024-04-28 17:38:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner {

	@Autowired
	private NettyServer nettyServer;

	@Override
	public void run(String... args) throws Exception {
		nettyServer.start();
	}

	public static void main(String[] args) {
		SpringApplication.run(NettyServerApplication.class, args);
	}
}