package com.wy.netty.file.client.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.netty.file.Result;
import com.wy.netty.file.utils.JSONUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpObject;
import io.netty.util.CharsetUtil;

/**
 * 客户端文件处理核心句柄
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:21:22
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FileClientHandler extends SimpleChannelInboundHandler<HttpObject> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileClientHandler.class);

	private boolean readingChunks;

	private final StringBuilder responseContent = new StringBuilder();

	private Result result;

	public void channelRead0(ChannelHandlerContext ctx, HttpObject object) throws Exception {
		if (!this.readingChunks) {
			DefaultFullHttpResponse response = (DefaultFullHttpResponse) object;
			LOGGER.info("STATUS: " + response.status());
			if ((response.status().code() == 200)) {
				this.readingChunks = true;
			} else {
				ByteBuf content = response.content();
				this.responseContent.append(content.toString(CharsetUtil.UTF_8));
			}
		} else {
			HttpChunkedInput chunk = (HttpChunkedInput) object;
			if (chunk.isEndOfInput()) {
				this.readingChunks = false;
				this.responseContent.append(chunk.readChunk(ctx.alloc()).content().toString(CharsetUtil.UTF_8));
				String json = this.responseContent.toString();
				this.result = ((Result) JSONUtil.parseObject(json, Result.class));
			} else {
				this.responseContent.append(chunk.readChunk(ctx.alloc()).content().toString(CharsetUtil.UTF_8));
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.getCause().printStackTrace();
		ctx.channel().close();
	}

	public Result getResult() {
		return this.result;
	}
}