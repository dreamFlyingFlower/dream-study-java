package com.wy.netty.file.client.support;

import com.wy.netty.file.Result;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 客户端处理
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:22:17
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FileClientPipelineFactory extends DefaultChannelPipeline {

	private FileClientHandler clientHandler;

	protected FileClientPipelineFactory(Channel channel) {
		super(channel);
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = channel().pipeline();
		this.clientHandler = new FileClientHandler();
		pipeline.addLast("codec", new HttpClientCodec());
		pipeline.addLast("inflater", new HttpContentDecompressor());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		pipeline.addLast("handler", this.clientHandler);
		return pipeline;
	}

	/**
	 * 处理结果
	 * 
	 * @return
	 */
	public Result getResult() {
		if (this.clientHandler != null) {
			return this.clientHandler.getResult();
		}
		return null;
	}
}