package com.wy.netty.http;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 
 * 
 * @author 飞花梦影
 * @date 2021-10-23 17:43:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NettyHttpResponse {

	private byte[] body;

	private FullHttpResponse httpResponse;

	/**
	 * 为了保证能够正确的获得字节数组,需要直接在构造中赋值字节数组
	 * 
	 * @param httpResponse
	 */
	public NettyHttpResponse(FullHttpResponse httpResponse) {
		this.httpResponse = httpResponse;
		if (httpResponse.content() != null) {
			body = new byte[httpResponse.content().readableBytes()];
			// 将响应中的字节数组转移到body中
			httpResponse.content().getBytes(0, body);
		}
	}

	public byte[] body() {
		return body;
	}

	public FullHttpResponse getResponse() {
		return httpResponse;
	}

	/**
	 * 错误方式.因为netty默认是0拷贝,使用的是直接内存,而直接内存都不支持array()
	 * 
	 * {@link UnpooledUnsafeDirectByteBuf#array}:该方法在直接内存中不支持,会抛异常
	 * 
	 * @return 字节数组
	 */
	// public byte[] body() {
	// return body = httpResponse.content() != null ? httpResponse.content().array()
	// : null;
	// }

	/**
	 * 错误方式.在NettyClientHttpHandler的channelRead0()调用完之后httpresponse就被释放了,同样无法获取body
	 * 
	 * @return 字节数组
	 */
	// public byte[] body() {
	// body = new byte[httpResponse.content().readableBytes()];
	// // 将响应中的字节数组转移到body中
	// httpResponse.content().getBytes(0, body);
	// return body;
	// }
}