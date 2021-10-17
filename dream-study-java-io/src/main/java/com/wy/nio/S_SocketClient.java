package com.wy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * NIO客户端
 * 
 * NIO客户端与服务器建立连接的流程:
 * 
 * <pre>
 * 1.打开SocketChannel
 * 2.设置SocketChannel为非阻塞模式,同时设置TCP参数
 * 3.异步连接服务器
 * 4.判断连接结果.如果成功,直接跳到10,否则执行5
 * 5.向Reactor线程的多路复用器注册OP_CONNECT事件
 * 6.创建Selector,启动线程
 * 7.Selector轮询就绪的Key
 * 8.handlerConnect()
 * 9.判断连接是否完成,若完成直接执行10
 * 10.向多路复用器注册读事件OP_READ
 * 11.handlerRead()异步读取请求消息到ByteBuffer中
 * 12.decode请求消息
 * 13.异步将ByteBuffer写到SocketChannel中
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-09-29 10:37:17
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_SocketClient {

	public static void main(String[] args) {
		InetSocketAddress remote = new InetSocketAddress("localhost", 18888);
		try (SocketChannel channel = SocketChannel.open(remote); Scanner scan = new Scanner(System.in);) {
			// 设置非阻塞模式
			channel.configureBlocking(false);
			ByteBuffer buffer = ByteBuffer.allocate(2014);
			while (true) {
				String line = scan.nextLine();
				if ("exit".equals(line)) {
					break;
				}
				buffer.put(line.getBytes(StandardCharsets.UTF_8));
				buffer.flip();
				// 发送数据给服务器
				channel.write(buffer);
				buffer.clear();
				// 读取服务器返回的消息
				int length = channel.read(buffer);
				if (length == -1) {
					break;
				}
				// 获得当前游标的位置
				// buffer.get();
				buffer.flip();
				byte[] datas = new byte[buffer.remaining()];
				buffer.get(datas);
				System.out.println(new String(datas, StandardCharsets.UTF_8));
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}