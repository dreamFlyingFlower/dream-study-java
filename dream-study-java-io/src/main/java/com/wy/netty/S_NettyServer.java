package com.wy.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.AbstractNioChannel;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultEventExecutorChooserFactory;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.concurrent.ThreadPerTaskExecutor;

/**
 * Netty服务端
 * 
 * Netty服务端创建:{@link ServerBootstrap#bind()}:用户代码入口<br>
 * ->doBind()<br>
 * ->->initAndRegister():初始化并注册Selector
 * ->->->newChannel():反射创建服务端channel,该channel就是BootStrap初始化时传入的{@link NioServerSocketChannel}<br>
 * ->->->->{@link NioServerSocketChannel}:反射调用该类的无参构造,再调用父类构造
 * ->->->->{@link AbstractNioChannel}:调用构造,再调用父类构造
 * ->->->->{@link AbstractChannel}:设置相应参数<br>
 * ->->->->{@link AbstractNioMessageChannel}:调用newUnsafe(),再调用父类构造<br>
 * ->->->->{@link AbstractChannel}:调用内部抽象类AbstractUnsafe的register(),该方法在initAndRegister()中被调用<br>
 * ->->->->{@link AbstractNioChannel}的doRegister(),jdk底层绑定channel<br>
 * ->->->->{@link AbstractChannel}的register0()的pipeline.fireChannelActive():传播事件
 * ->->->->{@link DefaultChannelPipeline}的HeadChannel类的fireChannelActive()
 * ->->->->{@link AbstractChannel}的beginRead()->{@link AbstractNioChannel}的doBeginRead():读事件的注册
 * 
 * ->->doBind0()->channel.eventLoop().execute()<br>
 * ->->->{@link SingleThreadEventExecutor#execute()}<br>
 * ->->->doStartThread()<br>
 * ->->->SingleThreadEventExecutor.this.run():该run()的实现类为{@link NioEventLoop},核心代码
 * ->->->->select():deadline以及任务穿插逻辑处理,避免空轮训的bug<br>
 * ->->->->processSelectedKeys():selected keySet优化<br>
 * ->->->->runAllTasks:task的分类添加,任务的聚合,任务的执行
 * 
 * ->->->->->processSelectedKeys(key,channel):检测新连接的入口
 * ->->->->->unsafe.read():实现类为{@link AbstractNioMessageChannel.NioMessageUnsafe}
 * ->->->->->doReadMessages():该方法的实现类为{@link NioServerSocketChannel}
 * ->->->->->SocketUtils.accept(javaChannel()):等待连接,并将连接初始化成{@link NioSocketChannel}
 * 
 * {@link NioEventLoopGroup}:该类中基本全是构造函数,设置基本的参数<br>
 * ->{@link MultithreadEventExecutorGroup}:该抽象类的构造函数中会将线程池赋值并进行一系列操作
 * ->->{@link ThreadPerTaskExecutor#execute}:每次都会新建一个新的线程来执行程序
 * ->->->{@link DefaultThreadFactory#newThread}:该方法就是上一步中execute实际调用的方法
 * ->->->{@link FastThreadLocalThread}:Netty封装的线程,是对ThreadLocal的优化实现
 * 
 * ->->newChild(),此处的实现类为{@link NioEventLoopGroup}<br>
 * ->->->{@link NioEventLoopGroup}的newChild()
 * ->->->{@link NioEventLoop}:构造,生成selector,任务队列Queue,该任务队列会传递到更上一层的父类赋值
 * 
 * ->->chooserFactory.newChooser(),此处实现类为{@link DefaultEventExecutorChooserFactory},对chooser进行处理
 * ->->{@link MultithreadEventExecutorGroup#next()}:当一个线程完成绑定之后下一个线程继续绑定,该方法会被
 * {@link ServerBootstrap.ServerBootstrapAcceptor#channelRead()}中的childGroup.register()调用
 * 
 * {@link ServerBootstrap#childHandler()}:需要传递一个{@link ChannelHandler}的实现类,此时的该类为
 * {@link ServerBootstrap.ServerBootstrapAcceptor}
 * 
 * {@link AbstractChannel}:构造函数->{@link DefaultChannelPipeline}:构造函数
 * ->>{@link DefaultChannelPipeline.TailContext}
 * ->>{@link DefaultChannelPipeline.HeadContext}
 * 
 * {@link ChannelInboundHandler#channelRead}:channelRead事件传播
 * ->{@link ChannelInboundHandlerAdapter}->{@link SimpleChannelInboundHandler}
 * ->{@link ChannelHandlerContext#writeAndFlush()}:写数据
 * ->write写buffer队列:direct化ByteBuf->插入写队列->设置写状态
 * ->flush刷新buffer队列:添加刷新标志并设置写状态->遍历buffer队列,过滤ByteBuf->调用jdk底层API进行自旋写
 * 
 * {@link ChannelOutboundHandler}:write事件传播,writeAndFlush()
 * ->{@link ChannelOutboundHandlerAdapter}
 * 
 * @author 飞花梦影
 * @date 2019-05-13 18:58:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_NettyServer {

	/** 监听线程组,监听客户端请求,构建线程组的时候,若不传递参数,则默认构建的线程组线程数是CPU核心数*2 */
	private EventLoopGroup acceptorGroup = new NioEventLoopGroup();

	/** 处理客户端相关操作线程组,负责处理与客户端的数据通讯 */
	private EventLoopGroup clientGroup = new NioEventLoopGroup();

	/** 服务启动相关配置信息,初始化服务的配置 */
	private ServerBootstrap bootstrap = new ServerBootstrap();

	public S_NettyServer() {
		init();
	}

	private void init() {
		try {
			// 设置配置信息,可链式调用,也可单独设置
			// 绑定线程组
			bootstrap.group(acceptorGroup, clientGroup)
					// 设定通讯模式为NIO,同步非阻塞
					.channel(NioServerSocketChannel.class)
					// 设置每一个TCP连接设置基本属性
					.childOption(ChannelOption.TCP_NODELAY, true)
					// 给每个TCP设置一些其他属性
					.childAttr(AttributeKey.newInstance("key"), "value")
					// 设定缓冲区大小,缓存区的单位是字节
					.option(ChannelOption.SO_BACKLOG, 1024)
					// SO_SNDBUF发送缓冲区,SO_RCVBUF接收缓冲区,SO_KEEPALIVE开启心跳监测(保证连接有效)
					.option(ChannelOption.SO_SNDBUF, 16 * 1024).option(ChannelOption.SO_RCVBUF, 16 * 1024)
					.option(ChannelOption.SO_KEEPALIVE, true).childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new S_AuthHandler());
						}
					}).bind(12345).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听处理逻辑
	 * 
	 * @param port 监听端口
	 * @param acceptorHandlers 处理器, 如何处理客户端请求
	 * @return
	 * @throws InterruptedException
	 */
	public ChannelFuture doAccept(int port, final ChannelHandler... acceptorHandlers) throws InterruptedException {

		/**
		 * childHandler是服务的Bootstrap中的方法,用于提供处理对象,可以一次性增加多个处理逻辑,类似责任链模式
		 * 增加A,B两个处理逻辑,在处理客户端请求数据的时候,根据A->B顺序依次处理
		 * 
		 * ChannelInitializer:用于提供处理器的一个模型对象.其中定义了initChannel(),该方法用于初始化处理逻辑责任链条,
		 * 保证服务端的Bootstrap只初始化一次处理器<br>
		 * 尽量提供处理逻辑的重用,避免反复的创建处理器对象,节约资源开销
		 */
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(acceptorHandlers);
			}
		});
		// 可以使用ChannelFuture实现后续的服务器和客户端的交互
		ChannelFuture future = bootstrap
				// 绑定监听端口.ServerBootstrap可以绑定多个监听端口,多次调用bind方法即可
				.bind(port)
				// 开始监听逻辑.返回一个ChannelFuture,返回结果代表的是监听成功后的一个对应的未来结果
				.sync();
		return future;
	}

	/**
	 * shutdownGracefully():是一个安全关闭的方法.可以保证不放弃任何一个已接收的客户端请求.
	 */
	public void release() {
		this.acceptorGroup.shutdownGracefully();
		this.clientGroup.shutdownGracefully();
	}

	public static void main(String[] args) {
		ChannelFuture future = null;
		S_NettyServer server = null;
		try {
			server = new S_NettyServer();
			future = server.doAccept(9999, new S_ServerHandler());
			System.out.println("server started.");
			// 关闭连接
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (null != future) {
				try {
					future.channel().closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (null != server) {
				server.release();
			}
		}
	}
}