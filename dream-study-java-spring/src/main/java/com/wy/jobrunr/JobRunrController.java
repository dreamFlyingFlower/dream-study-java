package com.wy.jobrunr;

import java.time.LocalDateTime;

import org.jobrunr.jobs.JobId;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link Job}:标注了该注解的类将在dashboard中显示
 *
 * @author 飞花梦影
 * @date 2025-08-09 14:57:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@RequestMapping("jobrunr")
@AllArgsConstructor
public class JobRunrController {

	final JobScheduler jobScheduler;

	final JobRunrService jobRunrService;

	/**
	 * 执行一次定时任务
	 * 
	 * @return
	 */
	@GetMapping("one")
	public ResponseEntity<?> one() {
		JobId enqueue = jobScheduler.enqueue(() -> jobRunrService.execute());
		return ResponseEntity.ok(enqueue.toString());
	}

	/**
	 * 未来执行一次定时任务
	 * 
	 * @return
	 */
	@GetMapping("future")
	public ResponseEntity<?> future() {
		JobId enqueue = jobScheduler.schedule(LocalDateTime.now().plusSeconds(30), () -> jobRunrService.execute());
		return ResponseEntity.ok(enqueue.toString());
	}
}