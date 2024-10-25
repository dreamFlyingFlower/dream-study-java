package dream.study.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * 待完成任务,内部使用topic进行细分,每个topic对应一个list集合
 *
 * @author 飞花梦影
 * @date 2021-10-29 16:43:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
@Slf4j
public class ReadyQueue {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	private String NAME = "process.queue";

	private String getKey(String topic) {
		return NAME + topic;
	}

	/**
	 * 获得队列
	 * 
	 * @param topic
	 * @return
	 */
	private BoundListOperations<Object, Object> getQueue(String topic) {
		return redisTemplate.boundListOps(getKey(topic));
	}

	/**
	 * 设置任务
	 * 
	 * @param delayJob
	 */
	public void pushJob(DelayJob delayJob) {
		log.info("执行队列添加任务:{}", delayJob);
		BoundListOperations<Object, Object> listOperations = getQueue(delayJob.getTopic());
		listOperations.leftPush(delayJob);
	}

	/**
	 * 移除并获得任务
	 * 
	 * @param topic
	 * @return
	 */
	public DelayJob popJob(String topic) {
		BoundListOperations<Object, Object> listOperations = getQueue(topic);
		Object o = listOperations.leftPop();
		if (o instanceof DelayJob) {
			log.info("执行队列取出任务:{}", JSON.toJSONString((DelayJob) o));
			return (DelayJob) o;
		}
		return null;
	}
}