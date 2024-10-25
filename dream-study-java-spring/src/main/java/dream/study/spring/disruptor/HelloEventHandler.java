package dream.study.spring.disruptor;

import com.lmax.disruptor.EventHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 构造EventHandler-消费者
 *
 * @author 飞花梦影
 * @date 2024-04-01 17:08:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class HelloEventHandler implements EventHandler<MessageDTO> {

	@Override
	public void onEvent(MessageDTO event, long sequence, boolean endOfBatch) {
		try {
			// 这里停止1000ms是为了确定消费消息是异步的
			Thread.sleep(1000);
			log.info("消费者处理消息开始");
			if (event != null) {
				log.info("消费者消费的信息是：{}", event);
			}
		} catch (Exception e) {
			log.info("消费者处理消息失败");
		}
		log.info("消费者处理消息结束");
	}
}