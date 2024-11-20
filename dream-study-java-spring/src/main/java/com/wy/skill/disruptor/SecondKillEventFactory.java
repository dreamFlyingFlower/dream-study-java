package com.wy.skill.disruptor;

import com.lmax.disruptor.EventFactory;
import com.wy.skill.SuccessKilled;

/**
 * 事件生成工厂,用来初始化预分配事件对象
 *
 * @author 飞花梦影
 * @date 2024-05-29 11:06:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SecondKillEventFactory implements EventFactory<SuccessKilled> {

	@Override
	public SuccessKilled newInstance() {
		return new SuccessKilled();
	}
}