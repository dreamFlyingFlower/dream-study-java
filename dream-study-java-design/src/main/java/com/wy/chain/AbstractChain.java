package com.wy.chain;

/**
 * 责任链模式:行为模式
 * 
 * A,B,C实现同一个接口或抽象类,同时接口中需要指定下一个对象,也可不指定;ABC只能处理指定类型的事件,
 * 被处理的事件可以同一个对象,但是需要被标识为不同的处理流程,也可以是不同的对象
 *
 * @author 飞花梦影
 * @date 2020-09-27 23:18:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public abstract class AbstractChain {

	// 被处理对象的标识
	private int identify;

	// 若不是某个实现类,需要交给下一个实现类
	private AbstractChain nextOne;

	public AbstractChain(int identify) {
		this.identify = identify;
	}

	public void setNextOne(AbstractChain nextOne) {
		this.nextOne = nextOne;
	}

	// t为需要处理的对象,可以是任意你想的
	public final void handlerMsg(NeedHandler t) {
		if (this.identify == t.getType()) {
			this.result(t);
		} else {
			if (this.nextOne != null) {
				this.nextOne.handlerMsg(t);
			} else {
				System.out.println("什么都没做...");
			}
		}
	}

	public abstract void result(NeedHandler t);
}