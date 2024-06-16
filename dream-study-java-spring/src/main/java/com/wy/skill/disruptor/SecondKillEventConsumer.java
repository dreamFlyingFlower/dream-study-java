package com.wy.skill.disruptor;

import com.lmax.disruptor.EventHandler;
import com.wy.skill.SecondKillService;
import com.wy.skill.SuccessKilled;

import dream.flying.flower.enums.TipEnum;
import dream.flying.flower.framework.web.helper.SpringContextHelpers;
import dream.flying.flower.result.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-29 11:08:25
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class SecondKillEventConsumer implements EventHandler<SuccessKilled> {

	private SecondKillService secondKillService = (SecondKillService) SpringContextHelpers.getBean("secondKillService");

	@Override
	public void onEvent(SuccessKilled seckillEvent, long seq, boolean bool) {
		Result<?> result = secondKillService.startDisruptor(seckillEvent.getSeckillId());
		if (result.equals(Result.ok(TipEnum.TIP_SUCCESS))) {
			log.info("用户:{}{}", seckillEvent.getUserId(), "秒杀成功");
		}
	}
}