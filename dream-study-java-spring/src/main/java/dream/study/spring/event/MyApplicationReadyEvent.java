package dream.study.spring.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * ApplicationReadyEvent
 * 
 * {@link ApplicationReadyEvent}:应用程序完全启动并处于可用状态后执行一些初始化逻辑.使用@EventListener注解或实现ApplicationListener接口来监听这个事件
 *
 * @author 飞花梦影
 * @date 2024-03-19 17:52:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MyApplicationReadyEvent {

	@EventListener(ApplicationReadyEvent.class)
	void readyListener(SpringApplication springApplication) {
		// 执行监听逻辑,此处object为ApplicationReadyEvent初始化时传入的参数,当前为SpringApplication
	}
}