package dream.study.spring.disruptor;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 构造DisruptorManager
 *
 * @author 飞花梦影
 * @date 2024-04-01 17:09:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class DisruptorManager {

	@Bean("messageModelRingBuffer")
	RingBuffer<MessageDTO> messageModelRingBuffer() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("customizerExecutor");
		executor.setThreadGroupName("customizer");
		executor.setMaxPoolSize(1000);
		executor.setCorePoolSize(500);
		executor.setQueueCapacity(2000);
		executor.setKeepAliveSeconds(60);
		// 线程池对拒绝任务(无线程可用)的处理策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		// 指定事件工厂
		HelloEventFactory factory = new HelloEventFactory();

		// 指定ringbuffer字节大小,必须为2的N次方(能将求模运算转为位运算提高效率),否则将影响效率
		int bufferSize = 1024 * 256;

		// 单线程模式,获取额外的性能
		Disruptor<MessageDTO> disruptor =
				new Disruptor<>(factory, bufferSize, executor, ProducerType.SINGLE, new BlockingWaitStrategy());

		// 设置事件业务处理器---消费者
		disruptor.handleEventsWith(new HelloEventHandler());

		// 启动disruptor线程
		disruptor.start();

		// 获取ringbuffer环,用于接取生产者生产的事件
		RingBuffer<MessageDTO> ringBuffer = disruptor.getRingBuffer();

		return ringBuffer;
	}
}