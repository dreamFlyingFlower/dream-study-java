package com.wy.flyweight;

/**
 * I图形类(具体享元角色)
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:17:25
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class IBox implements Box {

	@Override
	public String getShape() {
		return "I";
	}
}