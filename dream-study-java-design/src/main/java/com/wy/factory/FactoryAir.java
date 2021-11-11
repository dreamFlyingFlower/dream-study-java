package com.wy.factory;

import com.wy.entity.Air;
import com.wy.entity.Cartoon;

/**
 * 青空工厂类
 * 
 * @author 飞花梦影
 * @date 2021-11-03 09:32:20
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FactoryAir implements FactoryCartoon {

	@Override
	public Cartoon getCartoon() {
		return new Air();
	}
}