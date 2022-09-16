package com.wy.flyweight;

/**
 * L图形类(具体享元角色)
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:17:56
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class LBox implements Box {

	@Override
	public String getShape() {
		return "L";
	}
}