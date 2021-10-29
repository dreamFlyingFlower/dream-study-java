package com.wy.redis;

import lombok.Getter;

/**
 * 任务状态
 *
 * @author 飞花梦影
 * @date 2021-10-29 16:24:22
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
public enum TaskJobStatus {

	READY("可执行状态"),
	DELAY("不可执行状态，等待时钟周期"),
	RESERVED("已被消费者读取,但没有完成消费"),
	DELETED("已被消费完成或者已被删除");

	private String msg;

	private TaskJobStatus(String msg) {
		this.msg = msg;
	}
}