package com.wy.command;

public class CartoonCommand extends Command {

	public CartoonCommand(YoungMan youngMan) {
		super(youngMan);
	}

	@Override
	public void enjoy() {
		this.getYoungMan().enjoyCartoon();
	}
}