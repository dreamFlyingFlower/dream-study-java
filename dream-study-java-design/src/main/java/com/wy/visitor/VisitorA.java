package com.wy.visitor;

public class VisitorA implements Visitor {

	public void visit(Park park) {

	}

	public void visit(ParkA parkA) {
		System.out.println("清洁工A:完成公园" + parkA.getName() + "的卫生");
	}

	public void visit(ParkB parkB) {

	}
}