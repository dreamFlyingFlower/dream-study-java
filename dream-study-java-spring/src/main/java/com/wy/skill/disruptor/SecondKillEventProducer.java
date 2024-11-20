package com.wy.skill.disruptor;

import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;
import com.wy.skill.SuccessKilled;

/**
 * 使用translator方式生产者
 *
 * @author 飞花梦影
 * @date 2024-05-29 11:07:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SecondKillEventProducer {

	private final static EventTranslatorVararg<SuccessKilled> translator = (seckillEvent, seq, objs) -> {
		seckillEvent.setSeckillId((Long) objs[0]);
		seckillEvent.setUserId((Long) objs[1]);
	};

	private final RingBuffer<SuccessKilled> ringBuffer;

	public SecondKillEventProducer(RingBuffer<SuccessKilled> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void secondKill(long seckillId, long userId) {
		this.ringBuffer.publishEvent(translator, seckillId, userId);
	}
}