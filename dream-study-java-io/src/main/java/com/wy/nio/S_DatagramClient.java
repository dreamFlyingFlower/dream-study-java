package com.wy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;
import java.util.Scanner;

/**
 * UDP的socket通信
 * 
 * @author ParadiseWY
 * @date 2020-11-26 23:24:45
 * @git {@link https://github.com/mygodness100}
 */
public class S_DatagramClient {

	public static void main(String[] args) {
		try (DatagramChannel dc = DatagramChannel.open(); Scanner scan = new Scanner(System.in);) {
			dc.configureBlocking(false);
			ByteBuffer buf = ByteBuffer.allocate(1024);
			while (scan.hasNext()) {
				String str = scan.next();
				buf.put((new Date().toString() + ":\n" + str).getBytes());
				buf.flip();
				dc.send(buf, new InetSocketAddress("127.0.0.1", 9898));
				buf.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}