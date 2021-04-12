package com.wy.design.template;

/**
 * 模版模式:就是接口的多实现,然后在抽象类中做一些特殊操作,而实现类只需要关心个性化参数以及实现
 * 
 * @author ParadiseWY
 * @date 2020-09-28 20:42:13
 */
public interface Template {

	// 别管是手摇发动，还是电力发动，反正 是要能够发动起来，那这个实现要在实现类里了
	void start();

	// 能发动，那还要能停下来，那才是真本事
	void stop();

	// 喇叭会出声音，是滴滴叫，还是哔哔叫
	void alarm();

	// 引擎会轰隆隆的响，不响那是假的
	void engineBoom();
}