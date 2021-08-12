package com.wy.activity;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

/**
 * 实现Activiti监听器
 *
 * @author 飞花梦影
 * @date 2021-08-12 08:43:39
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MyActivitiListener implements ActivitiEventListener {

	@Override
	public void onEvent(ActivitiEvent event) {
		ActivitiEventType eventType = event.getType();
		// 根据各种事件类型的不同,决定不同的处理方式
		if (ActivitiEventType.PROCESS_STARTED.equals(eventType)) {
			// do something
		}
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}
}