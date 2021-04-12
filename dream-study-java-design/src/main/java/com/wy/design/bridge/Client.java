package com.wy.design.bridge;

/**
 * 桥接模式:多种类型搭配使用,自己调自己的超类的实现类,和装饰模式差不多
 * 
 * @author ParadiseWY
 * @date 2020-09-27 23:29:53
 */
public class Client {

	private Gift gift;

	public Client(Gift gift) {
		this.gift = gift;
	}

	public void give(Girl girl) {
		System.out.println(gift);
	}

	public static void main(String[] args) {
		Client client = new Client(new CarGift(new Flower()));
		client.give(new Girl());
	}
}