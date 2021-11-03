package com.wy.entity;

/**
 * 动漫-秦时明月实现类
 * 
 * @author 飞花梦影
 * @date 2021-11-03 10:00:07
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class QinMoon implements Cartoon {

	@Override
	public String country() {
		return "中国";
	}

	@Override
	public String name() {
		return "秦时明月";
	}

	@Override
	public String chineseName() {
		return "秦时明月";
	}

	@Override
	public String type() {
		return "武侠,历史,热血,权谋";
	}
}