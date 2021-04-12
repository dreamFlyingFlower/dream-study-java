package com.wy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * UDP的socket服务端
 * 
 * @author ParadiseWY
 * @date 2020-11-26 23:26:22
 * @git {@link https://github.com/mygodness100}
 */
public class S_DatagramServer {

	public static void main(String[] args) {
		try {
			DatagramChannel dc = DatagramChannel.open();
			dc.configureBlocking(false);
			dc.bind(new InetSocketAddress(9898));
			Selector selector = Selector.open();
			dc.register(selector, SelectionKey.OP_READ);
			while (selector.select() > 0) {
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey sk = it.next();
					if (sk.isReadable()) {
						ByteBuffer buf = ByteBuffer.allocate(1024);
						dc.receive(buf);
						buf.flip();
						System.out.println(new String(buf.array(), 0, buf.limit()));
						buf.clear();
					}
				}
				it.remove();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}