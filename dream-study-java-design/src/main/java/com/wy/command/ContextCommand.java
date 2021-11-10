package com.wy.command;

import java.util.ArrayList;
import java.util.List;

public class ContextCommand {

	private List<Command> commands = new ArrayList<Command>();

	public void setCommand(Command command) {
		commands.add(command);
	}

	public void removeCommand(Command command) {
		commands.remove(command);
	}

	public void enjoy() {
		for (Command command : commands) {
			command.enjoy();
		}
	}
}