package com.wy.abstracfactory;

import com.wy.entity.Cartoon;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-11-03 15:55:59
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AbstractChinaCartoonFactory implements AbstractFactoryCartoon {

	@Override
	public Cartoon getAir() {
		return new AbstractChinaAir();
	}

	@Override
	public Cartoon getClannad() {
		return new AbstractChinaClannad();
	}
}