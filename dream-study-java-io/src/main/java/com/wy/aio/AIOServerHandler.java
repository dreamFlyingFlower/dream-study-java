package com.wy.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * AIO服务端链接处理回调函数
 * 
 * @author 飞花梦影
 * @date 2019-04-23 12:10:16
 * @git {@link https://github.com/mygodness100}
 */
public class AIOServerHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

	/**
	 * 当请求到来后,监听成功,业务逻辑如何处理.必须使用accept方法,为下一次请求开启监听
	 * 
	 * @param result 和客户端直接建立关联的通道,有相关的数据
	 * @param attachment
	 */
	@Override
	public void completed(AsynchronousSocketChannel result, AioServer attachment) {
		// 处理下一次的客户端请求,类似递归逻辑
		attachment.getServerChannel().accept(attachment, this);
		handlerRead(result);
	}

	private void handlerRead(AsynchronousSocketChannel channel) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		/**
		 * AIO中的异步读操作
		 * 
		 * destination:目的地,是处理客户端传递数据的中转缓存,可以不使用<br>
		 * attachment:处理客户端传递数据的对象,通常使用buffer处理<br>
		 * handler:处理逻辑
		 */
		channel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {

			/**
			 * 业务逻辑,读取客户端传输的数据.
			 * 
			 * result:数据长度<br>
			 * attachment:buffer数据,在completed()执行时,OS已经将请求数据写入到Buffer中,但是未复位,使用前一定要复位
			 */
			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				attachment.flip();
				System.out.println(new String(attachment.array(), StandardCharsets.UTF_8));
				handlerWriter(channel);
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				exc.printStackTrace();
			}
		});
	}

	private void handlerWriter(AsynchronousSocketChannel channel) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		Scanner scan = new Scanner(System.in);
		String line = scan.nextLine();
		buffer.put(line.getBytes(StandardCharsets.UTF_8));
		// 必须复位
		buffer.flip();
		// write方法是一个异步操作,具体实现由OS实现.可以增加get方法,实现阻塞,等待OS的写操作结束
		channel.write(buffer);
		// channel.write(buffer).get(); // 调用get代表服务端线程阻塞,等待写操作完成
		scan.close();
	}

	/**
	 * 异常处理
	 * 
	 * @param exc
	 * @param attachment
	 */
	@Override
	public void failed(Throwable exc, AioServer attachment) {
		exc.printStackTrace();
	}
}