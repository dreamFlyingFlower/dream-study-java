package com.wy.facade;

public class Context {

	private Facade facadeA = new FacadeA();

	private Facade facadeB = new FacadeB();

	public void sendLetter(String content, String address) {
		facadeA.writeContext(content);
		facadeB.writeContext(content);

		facadeA.fillEnvelope(address);
		facadeB.fillEnvelope(address);

		facadeA.letterInotoEnvelope();
		facadeB.letterInotoEnvelope();

		facadeA.sendLetter();
		facadeB.sendLetter();
	}
}