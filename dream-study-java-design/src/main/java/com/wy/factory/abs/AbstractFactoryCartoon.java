package com.wy.factory.abs;

import com.wy.entity.Cartoon;

/**
 * 抽象工厂模式:为了实现更多个性化方法而区别于工厂模式,而且级别更多
 * 
 * 抽象工厂如果没有中间的抽象类,就和工厂模式一样.抽象类主要是为了个性化,做不同处理
 * 
 * 工厂模式主要是单个接口,抽象工厂是多个接口组合,每个工厂返回的实例接口可以一样,也可以不一样
 *
 * @author 飞花梦影
 * @date 2021-11-03 15:46:06
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface AbstractFactoryCartoon {

	Cartoon getAir();

	Cartoon getClannad();
}