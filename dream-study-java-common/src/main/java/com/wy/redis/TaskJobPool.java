package com.wy.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TaskJob任务池
 *
 * @author 飞花梦影
 * @date 2021-10-29 16:26:43
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class TaskJobPool {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	private static final String NAME = "job.pool";

	private BoundHashOperations<Object, Object, Object> getPool() {
		return redisTemplate.boundHashOps(NAME);
	}

	/**
	 * 添加任务
	 * 
	 * @param job
	 */
	public void addJob(TaskJob job) {
		log.info("任务池添加任务：{}", JSON.toJSONString(job));
		getPool().put(job.getId(), job);
		return;
	}

	/**
	 * 获得任务
	 * 
	 * @param jobId
	 * @return
	 */
	public TaskJob getJob(Long jobId) {
		Object o = getPool().get(jobId);
		if (o instanceof TaskJob) {
			return (TaskJob) o;
		}
		return null;
	}

	/**
	 * 移除任务
	 * 
	 * @param jobId
	 */
	public void removeDelayJob(Long jobId) {
		log.info("任务池移除任务：{}", jobId);
		// 移除任务
		getPool().delete(jobId);
	}
}