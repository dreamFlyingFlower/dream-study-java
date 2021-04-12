package com.wy.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * 管道:2个线程之间的单向数据连接,有一个source通道和以sink通道.数据从source读取,写到sink中
 * 
 * @author ParadiseWY
 * @date 2020-11-26 23:29:00
 * @git {@link https://github.com/mygodness100}
 */
public class S_Pipe {

	public static void main(String[] args) {
		try {
			// 获取管道
			Pipe pipe = Pipe.open();
			// 将缓冲区中的数据写入管道
			ByteBuffer buf = ByteBuffer.allocate(1024);
			Pipe.SinkChannel sinkChannel = pipe.sink();
			buf.put("通过单向管道发送数据".getBytes());
			buf.flip();
			sinkChannel.write(buf);

			// 读取缓冲区中的数据
			Pipe.SourceChannel sourceChannel = pipe.source();
			buf.flip();
			int len = sourceChannel.read(buf);
			System.out.println(new String(buf.array(), 0, len));
			sourceChannel.close();
			sinkChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}