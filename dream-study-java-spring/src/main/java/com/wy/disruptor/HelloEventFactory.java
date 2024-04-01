package com.wy.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 构造EventFactory
 *
 * @author 飞花梦影
 * @date 2024-04-01 17:07:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class HelloEventFactory implements EventFactory<MessageDTO> {

	@Override
	public MessageDTO newInstance() {
		return new MessageDTO();
	}
}