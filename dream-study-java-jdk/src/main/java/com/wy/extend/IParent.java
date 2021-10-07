package com.wy.extend;

/**
 * 
 * 
 * @author ParadiseWY
 * @date 2020-09-29 10:02:03
 */
public interface IParent {

	int richer = 500000;

	public static void name() {
		System.out.println("iparent static func");
	}

	public default void name1() {
		System.out.println("iparent default name1");
	}
}
