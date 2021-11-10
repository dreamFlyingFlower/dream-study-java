package com.wy.command;

public class TravelCommand extends Command {

	public TravelCommand(YoungMan youngMan) {
		super(youngMan);
	}

	@Override
	public void enjoy() {
		this.getYoungMan().enjoyTravel();
	}
}