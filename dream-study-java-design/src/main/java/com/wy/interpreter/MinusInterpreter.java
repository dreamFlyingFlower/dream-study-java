package com.wy.interpreter;

public class MinusInterpreter implements Interpreter {

	public void interpret(ContextInterpreter context) {
		System.out.println("自动递减");
		String input = context.getInput();
		int inInput = Integer.parseInt(input);
		--inInput;
		context.setInput(String.valueOf(inInput));
		context.setOutput(inInput);
	}
}