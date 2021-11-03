package com.wy.factory;

import com.wy.entity.Cartoon;

/**
 * 工厂模式
 * 
 * 接口Cartoon是最终需要得到的类,FactoryCartoon是获得Cartoon的工厂
 * 
 * 和简单工厂的区别在于多了一个获得具体实例的中间工厂,工厂是由类创建实例,抽象工厂是由接口创建实例的工厂,再创建实例
 * 
 * @author 飞花梦影
 * @date 2021-11-03 09:20:44
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface FactoryCartoon {

	Cartoon getCartoon();
}