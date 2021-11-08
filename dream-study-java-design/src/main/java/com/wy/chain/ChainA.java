package com.wy.chain;

public class ChainA extends AbstractChain {

	public ChainA() {
		super(1);
	}

	@Override
	public void result(NeedHandler t) {
		System.out.println("我处理类型为1的情况...");
	}
}