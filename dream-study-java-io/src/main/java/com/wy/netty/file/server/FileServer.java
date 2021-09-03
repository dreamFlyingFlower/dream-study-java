package com.wy.netty.file.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.wy.netty.file.server.support.FileServerPipelineFactory;

import io.netty.bootstrap.ServerBootstrap;

/**
 * 文件服务器netty启动类
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:55:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FileServer {

	private void run() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new FileServerPipelineFactory());
		bootstrap.bind(new InetSocketAddress(FileServerContainer.getInstance().getPort()));
	}

	public void init() {
		run();
	}
}