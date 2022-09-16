package com.wy.factory.abs;

import com.wy.entity.Cartoon;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-11-03 15:55:59
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AbstractJapanCartoonFactory implements AbstractFactoryCartoon {

	@Override
	public Cartoon getAir() {
		return new AbstractJapanAir();
	}

	@Override
	public Cartoon getClannad() {
		return new AbstractJapanClannad();
	}
}