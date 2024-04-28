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
 * {@link ChannelHandler}:顶级的事件处理接口
 * ->{@link ChannelOutboundHandler}:发消息的过程处理
 * ->{@link ChannelInboundHandler}:读取消息的过程处理
 * ->{@link ChannelInboundHandler#channelActive()}:通道就绪事件
 * ->{@link ChannelInboundHandler#channelRead()}:通道读取数据事件
 * ->{@link ChannelInboundHandler#channelReadComplete()}:通道读取数据完毕事件
 * ->{@link ChannelHandlerAdapter}:即可以读消息,也可以发送消息,是一个适配器
 * ->>{@link ChannelOutboundHandlerAdapter}:发消息的适配器
 * ->>{@link ChannelInboundHandlerAdapter}:读消息的适配器
 * </pre>
 * 
 * Netty两大性能优化工具类:<br>
 * {@link FastThreadLocal}:作用和 ThreadLocal 相当,但是更快.主要区别是用来存放数据的方式不一样: ThreadLocal用 ThreadLocalMap
 * 存数据,FastThreadLocal使用数组存数据 {@link Recycler}:实现了一个轻量级的对象池机制,即已经用过的对象将放入池子中,再用的时候直接从池子中取,使用单例
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