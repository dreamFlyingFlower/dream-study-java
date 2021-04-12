package com.wy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * nio客户端
 * 
 * @author ParadiseWY
 * @date 2020-09-29 10:37:17
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