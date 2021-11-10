package com.wy.mediator;

public abstract class MediatorPerson {

	private String name;

	private int condition;

	private Mediator mediator;

	public MediatorPerson(String name, int condition, Mediator mediator) {
		super();
		this.name = name;
		this.condition = condition;
		this.mediator = mediator;
	}

	public Mediator getMediator() {
		return mediator;
	}

	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}

	public abstract void getPartner(MediatorPerson person);
}