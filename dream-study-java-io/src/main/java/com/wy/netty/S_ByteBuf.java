package com.wy.netty;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.AbstractByteBufAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * {@link ByteBuf}:类似于NIO的ByteBuffer,但不是全部相同,主要方法是readXXX(),writeXXX(),setXXX()
 * 
 * read类方法会改变readIndex,write类方法会改变writeIndex,
 * set类方法不会改变readIndex和writeIndex,但是set和write写入的数据会相互覆盖
 * mark()和reset()功能同ByteBuffer中的mark()和reset(),都是标记功能,用来重读或重写数据
 * 
 * ByteBuf的主要操作类划分:<br>
 * Pooled开头的实现类:每次都从预先分配好的内存中取一段连续内存来封装成ByteBuf,即从JVM中进行内存分配
 * Unpooled开头的实现类:每次使用的时候直接调用API向系统申请内存,即使用CPU中的内存
 * Unsafe():可以直接拿到对象的内存地址,对数据进行读写操作<br>
 * 非Unsafe():不能拿到对象的内存地址<br>
 * Heap():直接对JVM中的堆内存进行操作,Pooled和Unpooled都包括了Heap和Direct<br>
 * Direct():直接调用JDK的API进行内存分配,不受JVM控制
 * 
 * {@link ByteBufAllocator}:顶层的ByteBuf内存分配管理器,safe和unsafe有系统自动判断
 * ->{@link AbstractByteBufAllocator}:通过构造参数实现上述ByteBuf的6种类型划分
 * ->>{@link UnpooledByteBufAllocator}:实现堆外内存分配使用API,newHeapBuffer()和newDirectBuffer(),用完需要释放
 * ->>{@link PooledByteBufAllocator}:实现堆内JVM内存分配使用,newHeapBuffer()和newDirectBuffer(),用完需要释放
 * 
 * 内存分配的几个重要概念:arena,chunk,page,subpage,由PoolThreadCache开始
 * 
 * @author ParadiseWY
 * @date 2020-11-29 15:01:34
 * @git {@link https://github.com/mygodness100}
 */
public class S_ByteBuf {

	public static void main(String[] args) {
		ByteBuf setInt = Unpooled.buffer().setInt(0, 1);
		System.out.println(setInt.getInt(2));
		System.out.println(setInt.writerIndex());
		System.out.println(setInt.getInt(0));
		byte[] array = setInt.array();
		System.out.println(new String(array, StandardCharsets.UTF_8));
		setInt.writeInt(10);
		System.out.println(setInt.getInt(0));
	}
}