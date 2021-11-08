package com.wy.interpreter;

/**
 * 上下文环境类,用来保存文法
 * 
 * @author 飞花梦影
 * @date 2021-11-08 23:10:37
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ContextInterpreter {

	private String input;

	private int output;

	public ContextInterpreter(String input) {
		this.input = input;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public int getOutput() {
		return output;
	}

	public void setOutput(int output) {
		this.output = output;
	}
}