package com.wy.jobrunr;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.filters.JobServerFilter;
import org.springframework.stereotype.Component;

/**
 * JobRunr过滤器.只需实现一个具有JobClientFilter或JobServerFilter接口的Bean即可,ApplyStateFilter和ElectStateFilter也可.
 * 
 * 在{@link Job}任务的filters中配置,可配置多个
 *
 * @author 飞花梦影
 * @date 2025-08-09 15:18:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
public class JobRunrFilter implements JobServerFilter {

	public void onProcessing(Job job) {
		// 处理中
	}

	public void onProcessed(Job job) {
		// 处理完成
	}
}