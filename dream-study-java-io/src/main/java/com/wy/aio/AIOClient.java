package com.wy.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * @description AIO客户端
 * @author ParadiseWY
 * @date 2019年4月23日 下午2:06:50
 * @git {@link https://github.com/mygodness100}
 */
public class AIOClient {

	private AsynchronousSocketChannel channel;

	public AIOClient(String host, int port) {
		init(host, port);
	}
	
	public static void main(String[] args) {
		AIOClient client = new AIOClient("localhost", 8889);
		Scanner s = new Scanner(System.in);
		String line = s.nextLine();
		client.write(line);
		client.read();
		s.close();
		try {
			client.channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init(String host, int port) {
		try {
			channel = AsynchronousSocketChannel.open();
			channel.connect(new InetSocketAddress(host, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String line) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(line.getBytes(StandardCharsets.UTF_8));
		buffer.flip();
		channel.write(buffer);
	}

	public void read() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			channel.read(buffer).get();
			buffer.flip();
			System.out.println(new String(buffer.array(),StandardCharsets.UTF_8));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}