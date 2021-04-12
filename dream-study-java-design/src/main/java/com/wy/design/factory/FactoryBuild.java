package com.wy.design.factory;

public class FactoryBuild {

	public static Factory build(Class<? extends Factory> factory) {
		try {
			return factory.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Factory a = FactoryBuild.build(FactoryA.class);
		Factory b = FactoryBuild.build(FactoryB.class);
		a.cry();
		b.cry();
	}
}