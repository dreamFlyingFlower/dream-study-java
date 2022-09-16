package com.wy.command;

/**
 * 命令模式:封装一个对象对另外一个对象的调用
 * 
 * <pre>
 * 整个调用过程比较繁杂,或者存在多处这种调用,使用Command类对该调用加以封装,便于功能的再利用
 * 调用前后需要对调用参数进行某些处理
 * 调用前后需要进行某些额外处理,比如日志,缓存,记录历史操作等
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-11-10 14:07:09
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public abstract class Command {

	/** 命令接收者 */
	private YoungMan youngMan;

	public Command(YoungMan youngMan) {
		this.youngMan = youngMan;
	}

	public YoungMan getYoungMan() {
		return youngMan;
	}

	public void setYoungMan(YoungMan youngMan) {
		this.youngMan = youngMan;
	}

	// 实际上应该是很复杂的业务
	abstract void enjoy();
}