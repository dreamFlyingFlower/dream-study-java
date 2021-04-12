package com.wy.nio;

import java.nio.ByteBuffer;

/**
 * NIO主要是缓冲区,主要类有{@link java.nio.Buffer},{@link java.nio.ByteBuffer},{@link java.nio.channels.Channels}
 * {@link java.nio.CharBuffer},{@link java.nio.ShortBuffer},{@link java.nio.IntBuffer}
 * {@link java.nio.LongBuffer},{@link java.nio.FloatBuffer},{@link java.nio.DoubleBuffer}
 * 
 * @apiNote 缓冲区的核心方法有2个:put()->写入数据,get()->读数据
 * 
 * @author ParadiseWY
 * @date 2020-11-26 00:04:52
 * @git {@link https://github.com/mygodness100}
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