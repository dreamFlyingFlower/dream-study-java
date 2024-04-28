package com.wy.netty;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * Netty解码器
 * 
 * <pre>
 * {@link ByteToMessageDecoder}:解码器,核心方法decode.将字节解码为消息(或另一个字节序列)
 * {@link FixedLengthFrameDecoder}:固定长度解码器分析
 * {@link LineBasedFrameDecoder}:基于行解码器分析,支持的行末尾\n,\r,\r\n
 * {@link DelimiterBasedFrameDecoder}:基于分隔符解码器分析
 * {@link LengthFieldBasedFrameDecoder}:基于长度域的解码器分析,主要有4个参数,见源码:
 * 		lengthFieldOffset:字节偏移量,在lengthFieldLength之前的字节数据长度
 * 		lengthFieldLength:整个数据长度值的16进制表示,该长度也会被包含在数据中,在整个数据前面,占字节数由数据整体长度决定
 * 		lengthAdjustment:在lengthFieldLength和正式数据之间的数据,有正负,正往后算,负往前算
 * 		initialBytesToStrip:整个数据需要跳过的字节长度,从字节的起始位置需要跳过的字节长度
 * 		整个数据的整体长度:lengthFieldLength所表示的10进制长度+lengthFieldOffset
 * 		真正要读的数据起始位置:从lengthFieldLength之后的字节开始+lengthAdjustment-initialBytesToStrip
 * 		真正要读的数据长度:整个数据的整体长度-真正要读的数据起始位置索引
 * 
 * {@link MessageToByteEncoder}:编码器,核心方法encode,匹配对象->分配内存->编码实现->释放对象->数据传播->释放内存
 * 
 * {@link ReplayingDecoder}:和ByteToMessageDecoder不同的是它允许子类实现decode() 和decodeLast()时,并不需要显式判断需要的字节是否已经全部接收到.会降低性能
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-04-28 13:41:48
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyNettyDecoder {

}