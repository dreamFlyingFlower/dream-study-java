package com.wy.tasks;

/**
 * 定时任务较少时可使用,动态控制修改定时任务
 *
 * @author 飞花梦影
 * @date 2024-04-19 17:55:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface TaskService {

	/**
	 * 执行方法
	 */
	void execute();

	/**
	 * 获取周期表达式
	 *
	 * @return CronExpression
	 */
	default String getCronExpression() {
		return null;
	}

	/**
	 * 获取任务名称
	 *
	 * @return 任务名称
	 */
	default String getTaskName() {
		return this.getClass().getSimpleName();
	}
}