package com.wy.netty.http;

import com.wy.result.ResultException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.DefaultPromise;

/**
 * 
 * 
 * @author 飞花梦影
 * @date 2021-10-23 17:30:46
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NettyClientHttpHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

	private DefaultPromise<NettyHttpResponse> promise;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
		if (msg.decoderResult().isFailure()) {
			throw new ResultException("请求失败:" + msg.decoderResult().cause());
		}
		NettyHttpResponse resp = new NettyHttpResponse(msg);
		// 调用完成,返回结果,唤醒NettyClientHttp的业务线程,当前方法执行完之后会释放msg
		promise.setSuccess(resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	public DefaultPromise<NettyHttpResponse> getPromise() {
		return promise;
	}

	public void setPromise(DefaultPromise<NettyHttpResponse> promise) {
		this.promise = promise;
	}
}