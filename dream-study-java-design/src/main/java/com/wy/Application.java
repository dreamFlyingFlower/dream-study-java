package com.wy;

import com.wy.abstracfactory.AbstractChinaCartoonFactory;
import com.wy.abstracfactory.AbstractFactoryCartoon;
import com.wy.abstracfactory.AbstractJapanCartoonFactory;
import com.wy.build.HeavenHandler;
import com.wy.build.HubeiHeaven;
import com.wy.decorator.Animal;
import com.wy.decorator.AnimalLand;
import com.wy.decorator.FlyAnimalDecorator;
import com.wy.decorator.WaterAnimalDecorator;
import com.wy.entity.Cartoon;
import com.wy.entity.HeavenNineSong;
import com.wy.factory.FactoryAir;
import com.wy.factory.FactoryCartoon;

/**
 * 24种设计模式:命令模式,类似门面模式;迭代模式:JDK中出现了迭代器,现在已经基本不用了
 * 
 * <pre>
 * 简单工厂模式:适用于简单的创建同一个接口的不同实现类的场景,主要是对象创建
 * 工厂模式:比简单工厂多了个创建接口的接口
 * </pre>
 * 
 * <pre>
 * 简单工厂->工厂:简单工厂只有一个接口,直接返回实例;工厂有2个接口,一个返回实例,一个返回实例工厂,通过实例工厂返回实例
 * 简单工厂->建造模式:简单工厂注重创建对象,而建造模式则是接口中多个属性或方法的设置调用
 * 组合模式:当整个项目呈现明显的树形结构时可使用,体现局部和整体的关系,例如人员关系表
 * 装饰模式:类似于JDK中的流,子类都实现一个接口或抽象类,同时类中会存在一个构造函数以接口类型为参数
 *          桥接模式是抽象类中声明一个自己的实现类变量,其他所有的子类继承抽象类或实现类
 * 工厂->策略:工厂注重对象的创建,而策略是注重行为的实现
 * 工厂->抽象工厂:抽象工厂多了中间的抽象类,对接口中的一些方法进行默认的实现
 * 策略->代理:策略模式的使用类不需要实现接口,而代理模式的实用类需要实现接口
 * 策略->门面:策略注重于单个方法的实现,而不是一系列方法的顺序实现,门面则是一系列特定操作的实现
 * 门面->模版:门面中所有的方法是按照特定的顺序执行,而模版中多了一个抽象类,
 *          抽象类负责通用方法的调用,而特殊方法则需要子类实现之后,通用方法中会穿插调用特殊方法
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-10-16 22:59:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Application {

	public static void main(String[] args) {
		// 工厂模式
		factory();
		// 抽象工厂模式
		abstractFactory();
		// 建造者模式
		builder();
		// 装饰器模式
		decorator();
	}

	public static void factory() {
		FactoryCartoon factoryCartoon = new FactoryAir();
		Cartoon cartoon = factoryCartoon.getCartoon();
		System.out.println(cartoon.chineseName());
	}

	public static void abstractFactory() {
		AbstractFactoryCartoon japanCartoon = new AbstractJapanCartoonFactory();
		System.out.println(japanCartoon.getAir().country());
		System.out.println(japanCartoon.getClannad().name());
		AbstractFactoryCartoon chainCartoon = new AbstractChinaCartoonFactory();
		System.out.println(chainCartoon.getAir().country());
		System.out.println(chainCartoon.getClannad().name());
	}

	public static void builder() {
		HubeiHeaven hubeiHeaven = new HubeiHeaven();
		HeavenHandler headler = new HeavenHandler();
		HeavenNineSong build = headler.build(hubeiHeaven);
		System.out.println(build.getName());
	}

	public static void decorator() {
		Animal animal = new AnimalLand();
		animal.show();
		System.out.println("---------");
		Animal waterAnimal = new WaterAnimalDecorator(animal);
		waterAnimal.show();
		System.out.println("---------");
		Animal flyWaterAnimal = new FlyAnimalDecorator(waterAnimal);
		flyWaterAnimal.show();
	}
}