package com.wy.facade;

/**
 * 门面A,可不实现Facade
 * 
 * @author 飞花梦影
 * @date 2022-09-16 15:14:57
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FacadeA implements Facade {

	@Override
	public void writeContext(String content) {
		System.out.println("A填写信的内容...." + content);
	}

	@Override
	public void fillEnvelope(String address) {
		System.out.println("A填写收件人地址及姓名...." + address);
	}

	@Override
	public void letterInotoEnvelope() {
		System.out.println("A把信放到信封中....");
	}

	@Override
	public void sendLetter() {
		System.out.println("A邮递信件...");
	}
}