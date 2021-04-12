package com.wy.design.facade;

public class Context {

	private Facade facade = new FacadeA();

	public void sendLetter(String content, String address) {
		facade.writeContext(content);

		facade.fillEnvelope(address);

		facade.letterInotoEnvelope();

		facade.sendLetter();
	}
}