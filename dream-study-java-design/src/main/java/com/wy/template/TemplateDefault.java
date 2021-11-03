package com.wy.template;

public abstract class TemplateDefault implements Template {
	final public void run() {
		// 先发动汽车
		this.start();
		// 引擎开始轰鸣
		this.engineBoom();
		// 喇嘛想让它响就响，不想让它响就不响
		if (this.isAlarm()) {
			this.alarm();
		}
		// 到达目的地就停车
		this.stop();
	}

	// 钩子方法，默认喇叭是会响的
	protected boolean isAlarm() {
		return true;
	}
}