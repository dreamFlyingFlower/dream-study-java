package com.wy.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author 飞花梦影
 * @date 2021-11-05 20:33:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class BeautyCartoon implements TypeCartoon {

	private List<TypeCartoon> children;

	public BeautyCartoon() {
		this.children = new ArrayList<>();
	}

	@Override
	public List<TypeCartoon> children() {
		return this.children;
	}

	@Override
	public String country() {
		return null;
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public String chineseName() {
		return null;
	}

	@Override
	public String type() {
		return null;
	}
}