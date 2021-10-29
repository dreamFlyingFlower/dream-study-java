package com.wy.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * 延迟任务,使用可排序的ZSet保存数据,提供取出最小值等操作
 *
 * @author 飞花梦影
 * @date 2021-10-29 16:30:53
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
@Slf4j
public class DelayBucket {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	private static final AtomicInteger INDEX = new AtomicInteger(0);

	@Value("${thread.size}")
	private int bucketsSize;

	private List<String> bucketNames = new ArrayList<>();

	@Bean
	public List<String> createBuckets() {
		for (int i = 0; i < bucketsSize; i++) {
			bucketNames.add("bucket" + i);
		}
		return bucketNames;
	}

	/**
	 * 获得桶的名称
	 * 
	 * @return
	 */
	private String getThisBucketName() {
		int thisIndex = INDEX.addAndGet(1);
		int i1 = thisIndex % bucketsSize;
		return bucketNames.get(i1);
	}

	/**
	 * 获得桶集合
	 * 
	 * @param bucketName
	 * @return
	 */
	private BoundZSetOperations<Object, Object> getBucket(String bucketName) {
		return redisTemplate.boundZSetOps(bucketName);
	}

	/**
	 * 放入延时任务
	 * 
	 * @param job
	 */
	public void addDelayJob(DelayJob job) {
		log.info("添加延迟任务:{}", JSON.toJSONString(job));
		String thisBucketName = getThisBucketName();
		BoundZSetOperations<Object, Object> bucket = getBucket(thisBucketName);
		bucket.add(job, job.getDelayDate());
	}

	/**
	 * 获得最新的延期任务
	 * 
	 * @return
	 */
	public DelayJob getFirstDelayTime(Integer index) {
		String name = bucketNames.get(index);
		BoundZSetOperations<Object, Object> bucket = getBucket(name);
		Set<TypedTuple<Object>> set = bucket.rangeWithScores(0, 1);
		if (CollectionUtils.isEmpty(set)) {
			return null;
		}
		TypedTuple<Object> typedTuple = set.stream().findFirst().get();
		Object value = typedTuple.getValue();
		if (value instanceof DelayJob) {
			return (DelayJob) value;
		}
		return null;
	}

	/**
	 * 移除延时任务
	 * 
	 * @param index
	 * @param delayJob
	 */
	public void removeDelayTime(Integer index, DelayJob delayJob) {
		String name = bucketNames.get(index);
		BoundZSetOperations<Object, Object> bucket = getBucket(name);
		bucket.remove(delayJob);
	}
}