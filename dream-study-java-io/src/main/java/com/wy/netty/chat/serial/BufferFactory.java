package com.wy.netty.chat.serial;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * buff工厂
 * 
 * @author -琴兽-
 *
 */
public class BufferFactory {

	public static ByteOrder BYTE_ORDER = ByteOrder.BIG_ENDIAN;

	private static ByteBufAllocator bufAllocator = PooledByteBufAllocator.DEFAULT;

	/**
	 * 获取一个buffer
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ByteBuf getBuffer() {
		ByteBuf buffer = bufAllocator.heapBuffer();
		buffer = buffer.order(BYTE_ORDER);
		return buffer;
	}

	/**
	 * 将数据写入buffer
	 * 
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ByteBuf getBuffer(byte[] bytes) {
		ByteBuf buffer = bufAllocator.heapBuffer();
		buffer = buffer.order(BYTE_ORDER);
		buffer.writeBytes(bytes);
		return buffer;
	}
}