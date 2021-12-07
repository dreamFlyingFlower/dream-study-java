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
 * {@link NioSocketChannel}:客户端使用,{@link NioServerSocketChannel}:服务端使用
 * 
 * Channel的主要实现和继承关系:
 * 
 * <pre>
 * {@link Channel}
 * ->{@link AbstractChannel}
 * ->>{@link AbstractNioChannel}
 * ->>>{@link AbstractNioByteChannel}
 * ->>>>{@link NioSocketChannel}
 * ->>>>>{@link NioSocketChannel.NioSocketChannelConfig}
 * 
 * ->>>{@link AbstractNioMessageChannel}
 * ->>>>{@link NioServerSocketChannel}
 * ->>>>>{@link NioServerSocketChannel.NioServerSocketChannelConfig}
 * 
 * {@link Channel.Unsafe}
 * ->{@link AbstractChannel.AbstractUnsafe}
 * ->>{@link AbstractNioChannel.AbstractNioUnsafe}
 * ->>>{@link AbstractNioByteChannel.NioByteUnsafe}
 * ->>>>{@link NioSocketChannel.NioSocketChannelUnsafe}
 * 
 * ->>>{@link AbstractNioMessageChannel.NioMessageUnsafe}
 * 
 * {@link ChannelHandler}
 * ->{@link ChannelOutboundHandler}
 * ->>{@link ChannelOutboundHandlerAdapter}
 * 
 * ->{@link ChannelHandlerAdapter}
 * ->>{@link ChannelOutboundHandlerAdapter}
 * ->>{@link ChannelInboundHandlerAdapter}
 * 
 * ->{@link ChannelInboundHandler}
 * ->>{@link ChannelInboundHandlerAdapter}
 * </pre>
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
 * {@link FastThreadLocal}:作用和 ThreadLocal 相当,但是更快.主要区别是用来存放数据的方式不一样:
 * ThreadLocal用 ThreadLocalMap 存数据,FastThreadLocal使用数组存数据
 * {@link Recycler}:实现了一个轻量级的对象池机制,即已经用过的对象将放入池子中,再用的时候直接从池子中取,使用单例
 * 
 * {@link ChannelPipeline}:向其中添加多个ChannelHandler时,前面的ChannelHandler处理完之后,将结果传给下一个ChannelHandler
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
 * Netty4内存泄露常见原因:
 * 
 * <pre>
 * 1.继承了{@link ChannelInboundHandlerAdapter},重写了channelRead(),读数据时没有主动释放消息内存,导致OOM
 * 2.服务端消息积压:一种是任务太多,队列无法处理过来;网络瓶颈,当发送速度超过网络链接能力时,导致发送队列积压.
 * 		为防止队列积压,在客户端做并发保护(高低水位机制)或者服务端进行流控.
 * 		高水位机制:当消息队列中积压的待发送消息总字节数到达高水位时,修改Channel状态为不可写
 * 		当积压的待发送字节数达到或者低于低水位时,修改Channel状态为可写
 * 3.当对端读取速度小于已发发送速度,导致自身TCP发送缓冲区满,频繁发生write 0字节时,待发送消息会在Netty队列中排队
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-28 14:36:35
 * @git {@link https://github.com/mygodness100}
 */
public class S_Netty {

}