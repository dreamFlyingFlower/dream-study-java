package com.wy.visitor;

public class ParkA implements ParkElement {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}