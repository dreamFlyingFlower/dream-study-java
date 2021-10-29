package com.wy.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务引用对象
 *
 * @author 飞花梦影
 * @date 2021-10-29 16:17:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DelayJob {

	/**
	 * 延迟任务的唯一标识
	 */
	private long jodId;

	/**
	 * 任务的执行时间
	 */
	private long delayDate;

	/**
	 * 任务类型（具体业务类型）
	 */
	private String topic;

	public DelayJob(TaskJob job) {
		this.jodId = job.getId();
		this.delayDate = System.currentTimeMillis() + job.getDelayTime();
		this.topic = job.getTopic();
	}

	public DelayJob(Object value, Double score) {
		this.jodId = Long.parseLong(String.valueOf(value));
		this.delayDate = System.currentTimeMillis() + score.longValue();
	}
}