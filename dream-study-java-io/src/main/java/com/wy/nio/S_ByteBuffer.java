package com.wy.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;

/**
 * NIO缓冲区,主要类有{@link Buffer},{@link ByteBuffer},{@link Channels},{@link CharBuffer},
 * {@link ShortBuffer},{@link IntBuffer},{@link LongBuffer},{@link FloatBuffer},{@link DoubleBuffer}
 * 
 * 缓冲区的核心方法有2个:put()->写入数据,get()->读数据
 * 
 * @author 飞花梦影
 * @date 2020-11-26 00:04:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_ByteBuffer {

	public static void main(String[] args) {
		String str = "abcde";
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put(str.getBytes());
		// 将position置为0,limit置为刚才position的位置,以便读取数据
		buf.flip();
		byte[] dst = new byte[buf.limit()];
		buf.get(dst, 0, 2);
		System.out.println(new String(dst, 0, 2));
		System.out.println(buf.position());
		// mark():标记
		buf.mark();
		buf.get(dst, 2, 2);
		System.out.println(new String(dst, 2, 2));
		System.out.println(buf.position());
		// reset():恢复到mark的位置
		buf.reset();
		System.out.println(buf.position());
		// 判断缓冲区中是否还有剩余数据
		if (buf.hasRemaining()) {
			// 获取缓冲区中可以操作的数量
			System.out.println(buf.remaining());
		}
	}
}