package com.wy.extend;

/**
 * 
 * 
 * @author ParadiseWY
 * @date 2020-09-29 10:02:09
 */
public class ISon implements IParent {

	public int richer = 50;

	public static void name() {
		System.out.println("ison static func");
	}
	
	public void name1() {
		System.out.println("ison default name1");
	}
}