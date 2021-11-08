package com.wy.interpreter;

/**
 * 解释器模式:行为模式,用来解释预先定义的文法
 * 
 * @author 飞花梦影
 * @date 2021-11-08 23:07:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface Interpreter {

	void interpret(ContextInterpreter context);
}