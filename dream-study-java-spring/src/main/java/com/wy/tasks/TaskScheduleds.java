package com.wy.tasks;

import java.util.Date;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务类,需要在配置文件中开启对应的注解{@link EnableScheduling},但是不能动态修改定时任务表达式
 * 
 * 定时任务默认是阻塞的,可以使用CompletableFuture类或添加Async注解使定时任务变为异步任务<br>
 * 当定时任务为阻塞时,配置文件为spring.task.scheduling;异步时为spring.task.execution
 * 
 * cron表达式可写6或7位,分别是秒,分,时,日,月,周,年,年可默认不写<br>
 * 通配符,以秒为例子:<br>
 * -:区间,如1-10,表示1,2,3...一直到10的时间段都生效<br>
 * ,:多个值,如1,3,4,表示1秒,3秒,4秒才生效,其他时间不生效<br>
 * *:当前位置的每个值都会触发<br>
 * ?:不关心当前位置的值<br>
 * /:间隔多少时间单位触发,如分钟上5/10,表示从第5分钟开始触发,隔10分钟触发一次<br>
 * 
 * 秒:0-59,通配符:, - * / <br>
 * 分:0-59,通配符:, - * / <br>
 * 时:0-23,通配符:, - * / <br>
 * 日:0-31,通配符:, - * / ? L(每个月最后一天) W(有效的工作日) <br>
 * 月:1-12或对应的英文简写,通配符:, - * / <br>
 * 周:1-7或对应的英文简写,1代表周天,2代表周1,通配符:, - * / ? L #(用来指定第几个周几,4#3表示每个月的第3个星期三)<br>
 * 年:1970-,通配符:, - * / <br>
 *
 * @author ParadiseWY
 * @date 2017-12-03 23:49:49
 * @git {@link https://github.com/mygodness100}
 */
@Component
@Async
public class TaskScheduleds {

	/**
	 * Scheduled中cron只能单独写注解,不能和fixedDelay,initialDelay同时写一个注解 fixedDelay:2次方法调用之间的间隔
	 * initialDelay:第一次调用本方法延迟的时间
	 */
	@Scheduled(fixedDelay = 5000l, initialDelay = 2000l)
	public void currentTime() {
		System.out.println(new Date());
	}

	/**
	 * 每秒调用一次
	 */
	@Scheduled(cron = "* * * * * ?")
	public void everySecond() {
		System.out.println("每秒调用一次");
	}

	/**
	 * 每分钟调用一次
	 */
	@Scheduled(cron = "0 * * * * ?")
	public void everyMinute() {
		System.out.println("每分钟调用一次");
	}

	/**
	 * 每3秒调用一次
	 */
	@Scheduled(cron = "0/3 * * * * ?")
	public void threeSecond() {
		System.out.println("每3秒调用一次");
	}

	/**
	 * 每分钟的前6秒,每秒调用一次
	 */
	@Scheduled(cron = "0-6 * * * * ?")
	public void sixSecond() {
		System.out.println("前6秒调用一次");
	}
}