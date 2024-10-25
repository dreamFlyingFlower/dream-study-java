package dream.study.spring.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lmax.disruptor.RingBuffer;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-04-01 17:18:25
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
public class DisruptorServiceImpl implements DisruptorService {

	@Autowired
	private RingBuffer<MessageDTO> messageModelRingBuffer;

	@Override
	public void sayHello(String message) {
		log.info("record the message: {}", message);
		// 获取下一个Event槽的下标
		long sequence = messageModelRingBuffer.next();
		try {
			// 给Event填充数据
			MessageDTO event = messageModelRingBuffer.get(sequence);
			event.setMessage(message);
			log.info("往消息队列中添加消息：{}", event);
		} catch (Exception e) {
			log.error("failed to add event to messageModelRingBuffer for : e = {},{}", e, e.getMessage());
		} finally {
			// 发布Event,激活观察者去消费,将sequence传递给改消费者
			// 注意最后的publish方法必须放在finally中以确保必须得到调用;如果某个请求的sequence未被提交将会堵塞后续的发布操作或者其他的producer
			messageModelRingBuffer.publish(sequence);
		}
	}
}