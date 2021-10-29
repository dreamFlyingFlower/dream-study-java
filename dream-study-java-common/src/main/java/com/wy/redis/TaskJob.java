package com.wy.redis;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务
 *
 * @author 飞花梦影
 * @date 2021-10-29 16:20:08
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskJob implements Serializable {

	private static final long serialVersionUID = 6746882300631510672L;

	/**
	 * 延迟任务的唯一标识,用于检索任务
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 具体业务类型
	 */
	private String topic;

	/**
	 * 任务的延迟时间
	 */
	private Long delayTime;

	/**
	 * 任务执行的超时时间
	 */
	private Long ttrTime;

	/**
	 * 任务的具体消息内容
	 */
	private String message;

	/**
	 * 重试次数
	 */
	private Integer retry;

	/**
	 * 任务状态
	 */
	private TaskJobStatus status;
}