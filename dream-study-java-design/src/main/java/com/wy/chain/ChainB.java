package com.wy.chain;

public class ChainB extends AbstractChain {

	public ChainB() {
		super(2);
	}

	@Override
	public void result(NeedHandler t) {
		System.out.println("我处理类型为2的情况...");
	}
}