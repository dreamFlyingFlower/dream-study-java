package com.wy.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @description 
 * @author ParadiseWY
 * @date 2019年4月23日 下午12:10:16
 * @git {@link https://github.com/mygodness100}
 */
public class AIOServerHandler implements CompletionHandler<AsynchronousSocketChannel,AioServer> {

	/**
	 * 当请求到来后,监听成功,业务逻辑如何处理.必须使用accept方法,为下一次请求开启监听
	 * @param result 和客户端直接建立关联的通道,有相关的数据
	 * @param attachment
	 */
	@Override
	public void completed(AsynchronousSocketChannel result, AioServer attachment) {
		// 处理下一次的客户端请求,类似递归逻辑
		attachment.getServerChannel().accept(attachment,this);
		handlerRead(result);
	}

	private void handlerRead(AsynchronousSocketChannel channel) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		/**
		 * AIO中的异步读操作
		 * destination 目的地,是处理客户端传递数据的中转缓存,可以不使用
		 * attachment 处理客户端传递数据的对象,通常使用buffer
		 * handler 处理逻辑
		 */
		channel.read(buffer, buffer, new CompletionHandler<Integer,ByteBuffer>() {

			/**
			 * 业务逻辑,读取客户端传输的数据
			 * result 数据长度
			 * attachment buffer数据
			 */
			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				attachment.flip();
				System.out.println(new String(attachment.array(),StandardCharsets.UTF_8));
				handlerWriter(channel);
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				
			}
		});
	}

	private void handlerWriter(AsynchronousSocketChannel channel) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		Scanner scan = new Scanner(System.in);
		String line = scan.nextLine();
		buffer.put(line.getBytes(StandardCharsets.UTF_8));
		buffer.flip();
		channel.write(buffer);
		scan.close();
	}

	/**
	 * 异常处理
	 * @param exc
	 * @param attachment
	 */
	@Override
	public void failed(Throwable exc, AioServer attachment) {
		exc.printStackTrace();
	}
}