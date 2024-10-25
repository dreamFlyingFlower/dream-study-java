package dream.study.spring.tasks.example;

import java.util.Date;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务类,需要开启{@link EnableScheduling},但是不能动态修改定时任务表达式
 * 
 * 定时任务默认是阻塞的,可以使用{@link CompletableFuture}类或添加{@link Async}使定时任务变为异步任务
 * 
 * 当定时任务为阻塞时,配置文件为spring.task.scheduling;异步时为spring.task.execution
 * 
 * cron表达式:
 * 
 * <pre>
 * 可写6或7位,分别是秒,分,时,日,月,周,年,年可默认不写
 * 通配符,以秒为例子:
 * -:区间,如1-10,表示1,2,3...一直到10的时间段都生效
 * ,:多个值,如1,3,4,表示1秒,3秒,4秒才生效,其他时间不生效
 * *:当前位置的每个值都会触发
 * ?:不关心当前位置的值
 * /:间隔多少时间单位触发,如分钟上5/10,表示从第5分钟开始触发,隔10分钟触发一次
 * </pre>
 * 
 * cron表达式每个参数可能的值:
 * 
 * <pre>
 * 秒:0-59,通配符:, - * /
 * 分:0-59,通配符:, - * /
 * 时:0-23,通配符:, - * /
 * 日:0-31,通配符:, - * / ? L(每个月最后一天) W(有效的工作日)
 * 月:1-12或对应的英文简写,通配符:, - * /
 * 周:1-7或对应的英文简写,1代表周天,2代表周1,通配符:, - * / ? L #(用来指定第几个周几,4#3表示每个月的第3个星期三)
 * 年:1970-,通配符:, - * /
 * </pre>
 * 
 * {@link Scheduled}:表明被修饰的方法是一个定时任务
 * 
 * <pre>
 * {@link Scheduled#cron()}:cron表达式,不能和fixedDelay,initialDelay同时使用
 * {@link Scheduled#fixedDelay()}:2次方法调用之间的间隔
 * {@link Scheduled#initialDelay()}:第一次调用定时任务方法延迟的时间
 * </pre>
 *
 * @author 飞花梦影
 * @date 2017-12-03 23:49:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Async
public class TaskScheduleds {

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