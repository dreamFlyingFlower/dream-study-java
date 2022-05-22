package com.wy.interpreter;

public class PlusInterpreter implements Interpreter {

	@Override
	public void interpret(ContextInterpreter context) {
		System.out.println("自动递增");
		String input = context.getInput();
		int intInput = Integer.parseInt(input);
		++intInput;
		// 对上下文环境重新赋值ֵ
		context.setInput(String.valueOf(intInput));
		context.setOutput(intInput);
	}
}