package dream.study.spring.skill.disruptor;

import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import dream.study.spring.skill.SuccessKilled;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-29 11:12:14
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DisruptorUtil {

	static Disruptor<SuccessKilled> disruptor;

	static {
		SecondKillEventFactory factory = new SecondKillEventFactory();
		int ringBufferSize = 1024;
		ThreadFactory threadFactory = runnable -> new Thread(runnable);
		disruptor = new Disruptor<>(factory, ringBufferSize, threadFactory);
		disruptor.handleEventsWith(new SecondKillEventConsumer());
		disruptor.start();
	}

	public static void producer(SuccessKilled kill) {
		RingBuffer<SuccessKilled> ringBuffer = disruptor.getRingBuffer();
		SecondKillEventProducer producer = new SecondKillEventProducer(ringBuffer);
		producer.secondKill(kill.getSeckillId(), kill.getUserId());
	}
}