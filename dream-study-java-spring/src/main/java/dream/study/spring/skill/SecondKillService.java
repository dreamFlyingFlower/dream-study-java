package dream.study.spring.skill;

import java.util.Random;

import org.springframework.stereotype.Service;

import dream.flying.flower.result.Result;
import dream.study.spring.skill.disruptor.DisruptorUtil;
import dream.study.spring.skill.queue.SecondKillQueue;
import lombok.extern.slf4j.Slf4j;

/**
 * 秒杀业务服务
 *
 * @author 飞花梦影
 * @date 2024-05-29 10:49:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@Slf4j
public class SecondKillService {

	/**
	 * 出现超卖问题
	 * 
	 * @param skgId
	 * @return
	 */
	public Result<?> startQueue(long skgId) {
		try {
			log.info("开始秒杀方式六...");
			final long userId = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;
			SuccessKilled kill = new SuccessKilled();
			kill.setSeckillId(skgId);
			kill.setUserId(userId);
			Boolean flag = SecondKillQueue.getSkillQueue().produce(kill);
			// 虽然进入了队列,但是不一定能秒杀成功 进队出队有时间间隙
			if (flag) {
				log.info("用户:{}{}", kill.getUserId(), "秒杀成功");
			} else {

				log.info("用户:{}{}", userId, "秒杀失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.ok();
	}

	/**
	 * 与自定义队列有着同样的问题,也会出现超卖的情况,但效率有所提高
	 * 
	 * @param skgId
	 * @return
	 */
	public Result<?> startDisruptor(long skgId) {
		try {
			log.info("开始秒杀方式七...");
			final long userId = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000;
			SuccessKilled kill = new SuccessKilled();
			kill.setSeckillId(skgId);
			kill.setUserId(userId);
			DisruptorUtil.producer(kill);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.ok();
	}
}