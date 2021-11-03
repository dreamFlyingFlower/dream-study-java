package com.wy.factory;

import com.wy.entity.Cartoon;
import com.wy.entity.Clannad;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-11-03 09:36:57
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FactoryClannad implements FactoryCartoon {

	@Override
	public Cartoon getCartoon() {
		return new Clannad();
	}
}