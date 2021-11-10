package com.wy.mediator;

public class MediatorMan extends MediatorPerson {

	public MediatorMan(String name, int condition, Mediator mediator) {
		super(name, condition, mediator);
	}

	public void getPartner(MediatorPerson person) {
		this.getMediator().setMan(this);
		this.getMediator().getPartner(person);
	}
}