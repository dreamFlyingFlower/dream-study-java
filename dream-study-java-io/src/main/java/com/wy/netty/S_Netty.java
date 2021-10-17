package com.wy.netty;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.nio.AbstractNioChannel;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.Recycler;
import io.netty.util.concurrent.FastThreadLocal;

/**
 * Netty的基本组件:{@link NioEventLoop#run},相当于传统BIO中的Thread,该类也实现了{@link Executor}
 * {@link Channel},相当于NIO中的{@link java.nio.channels.Channel},用来操作{@link Socket}
 * {@link ByteBuf},相当于NIO中的{@link ByteBuffer};{@link ChannelHandler},业务逻辑操作
 * 
 * Channel的主要实现和继承关系:<br>
 * {@link Channel}<br>
 * ->{@link AbstractChannel}<br>
 * ->>{@link AbstractNioChannel}<br>
 * ->>>{@link AbstractNioByteChannel}<br>
 * ->>>>{@link NioSocketChannel}<br>
 * ->>>>>{@link NioSocketChannel.NioSocketChannelConfig}<br>
 * 
 * ->>>{@link AbstractNioMessageChannel}<br>
 * ->>>>{@link NioServerSocketChannel}<br>
 * ->>>>>{@link NioServerSocketChannel.NioServerSocketChannelConfig}<br>
 * 
 * {@link Channel.Unsafe}<br>
 * ->{@link AbstractChannel.AbstractUnsafe}<br>
 * ->>{@link AbstractNioChannel.AbstractNioUnsafe}<br>
 * ->>>{@link AbstractNioByteChannel.NioByteUnsafe}<br>
 * ->>>>{@link NioSocketChannel.NioSocketChannelUnsafe}<br>
 * 
 * ->>>{@link AbstractNioMessageChannel.NioMessageUnsafe}<br>
 * 
 * {@link ChannelHandler}<br>
 * ->{@link ChannelOutboundHandler}<br>
 * ->>{@link ChannelOutboundHandlerAdapter}<br>
 * 
 * ->{@link ChannelHandlerAdapter}<br>
 * ->>{@link ChannelOutboundHandlerAdapter}<br>
 * ->>{@link ChannelInboundHandlerAdapter}<br>
 * 
 * ->{@link ChannelInboundHandler}<br>
 * ->>{@link ChannelInboundHandlerAdapter}<br>
 * 
 * {@link ByteToMessageDecoder}:解码器,核心方法decode
 * ->{@link FixedLengthFrameDecoder}:固定长度解码器分析
 * ->{@link LineBasedFrameDecoder}:基于行解码器分析,支持的行末尾\n,\r,\r\n
 * ->{@link DelimiterBasedFrameDecoder}:基于分隔符解码器分析
 * ->{@link LengthFieldBasedFrameDecoder}:基于长度域的解码器分析,主要有4个参数,见源码:<br>
 * lengthFieldOffset:字节偏移量,在lengthFieldLength之前的字节数据长度
 * lengthFieldLength:整个数据长度值的16进制表示,该长度也会被包含在数据中,在整个数据前面,占字节数由数据整体长度决定
 * lengthAdjustment:在lengthFieldLength和正式数据之间的数据,有正负,正往后算,负往前算
 * initialBytesToStrip:整个数据需要跳过的字节长度,从字节的起始位置需要跳过的字节长度
 * 整个数据的整体长度:lengthFieldLength所表示的10进制长度+lengthFieldOffset
 * 真正要读的数据起始位置:从lengthFieldLength之后的字节开始+lengthAdjustment-initialBytesToStrip
 * 真正要读的数据长度:整个数据的整体长度-真正要读的数据起始位置下标
 * 
 * {@link MessageToByteEncoder}:编码器,核心方法encode,匹配对象->分配内存->编码实现->释放对象->数据传播->释放内存
 * 
 * Netty两大性能优化工具类:<br>
 * {@link FastThreadLocal}:作用和ThreadLocal相当,但是更快
 * {@link Recycler}:实现了一个轻量级的对象池机制,即已经用过的对象将放入池子中,再用的时候直接从池子中取,适用单例
 * 
 * {@link ChannelPipeline}:向其中添加多个ChannelHandler时,前面的ChannelHandler处理完成之后,将结果传给下一个ChannelHandler
 * 
 * Netty3->Netty4的变化:
 * 
 * <pre>
 * ChannelBuffer->{@link ByteBuf}
 * ChannelBuffers->池化类:{@link PooledByteBufAllocator},池化类
 * 		非池化类:{@link UnpooledByteBufAllocator},{@link Unpooled}等Unpool开头的类
 * 		池化类效率高于非池化内存空间分配类,不管是池化还是非池化类,使用完后需要释放
 * FrameDecoder->{@link ByteToMessageDecoder}
 * FrameEncoder->{@link MessageToByteEncoder}
 * OneToOneEncoder->{@link MessageToByteEncoder}
 * messageReceive->channelRead0(netty5里面是messageReceive)
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-28 14:36:35
 * @git {@link https://github.com/mygodness100}
 */
public class S_Netty {

}