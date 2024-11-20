package com.wy.skill.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.wy.skill.SecondKillService;
import com.wy.skill.SuccessKilled;

import dream.flying.flower.enums.TipEnum;
import dream.flying.flower.result.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * 程序启动就启动秒杀队列
 *
 * @author 飞花梦影
 * @date 2024-05-29 10:48:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@Component
public class SecondKillQueueRunner implements ApplicationRunner {

	@Autowired
	private SecondKillService seckillService;

	@Override
	public void run(ApplicationArguments var) {

		new Thread(() -> {

			log.info("队列启动成功");
			while (true) {

				try {
					// 进程内队列
					SuccessKilled kill = SecondKillQueue.getSkillQueue().consume();
					if (kill != null) {
						Result<?> result = seckillService.startQueue(kill.getSeckillId());
						if (result != null && result.equals(Result.ok(TipEnum.TIP_SUCCESS.getValue()))) {
							log.info("TaskRunner,result:{}", result);
							log.info("TaskRunner从消息队列取出用户，用户:{}{}", kill.getUserId(), "秒杀成功");
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}