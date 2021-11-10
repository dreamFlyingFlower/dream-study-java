package com.wy.mediator;

public class MediatorWoman extends MediatorPerson {

	public MediatorWoman(String name, int condition,Mediator mediator) {
		super(name, condition, mediator);
	}

	public void getPartner(MediatorPerson person) {
		this.getMediator().setWoman(this);
		this.getMediator().getPartner(person);
	}
}