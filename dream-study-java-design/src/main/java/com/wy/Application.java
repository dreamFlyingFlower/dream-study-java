package com.wy;

import java.util.ArrayList;
import java.util.List;

import com.wy.abstracfactory.AbstractChinaCartoonFactory;
import com.wy.abstracfactory.AbstractFactoryCartoon;
import com.wy.abstracfactory.AbstractJapanCartoonFactory;
import com.wy.adapter.Adapter;
import com.wy.adapter.AdapterA;
import com.wy.adapter.Text;
import com.wy.bridge.CarGift;
import com.wy.bridge.FlowerSend;
import com.wy.build.HeavenHandler;
import com.wy.build.HubeiHeaven;
import com.wy.chain.AbstractChain;
import com.wy.chain.ChainA;
import com.wy.chain.ChainB;
import com.wy.chain.NeedHandler;
import com.wy.composite.BeautyCartoon;
import com.wy.composite.MyGodness;
import com.wy.composite.TypeCartoon;
import com.wy.decorator.Animal;
import com.wy.decorator.AnimalLand;
import com.wy.decorator.FlyAnimalDecorator;
import com.wy.decorator.WaterAnimalDecorator;
import com.wy.entity.Cartoon;
import com.wy.entity.HeavenNineSong;
import com.wy.factory.FactoryAir;
import com.wy.factory.FactoryCartoon;
import com.wy.interpreter.ContextInterpreter;
import com.wy.interpreter.Interpreter;
import com.wy.interpreter.MinusInterpreter;
import com.wy.interpreter.PlusInterpreter;
import com.wy.strategy.Context;
import com.wy.strategy.StrategyA;
import com.wy.strategy.StrategyB;

/**
 * 24种设计模式:命令模式,类似门面模式;迭代模式:JDK中出现了迭代器,现在已经基本不用了
 * 
 * https://www.cnblogs.com/shoshana-kong/p/10787629.html
 * https://www.cnblogs.com/yangsy0915/p/5117522.html
 * https://blog.csdn.net/zhao135897/article/details/83344650
 * 
 * <pre>
 * 简单工厂模式:适用于简单的创建同一个接口的不同实现类的场景,主要是对象创建
 * 工厂模式:比简单工厂多了个创建接口的接口
 * 享元模式:实际上就是利用Map进行对象缓存
 * </pre>
 * 
 * <pre>
 * 简单工厂->工厂:简单工厂只有一个接口,直接返回实例;工厂有2个接口,一个返回实例,一个返回实例工厂,通过实例工厂返回实例
 * 简单工厂->建造模式:简单工厂注重创建对象,而建造模式则是接口中多个属性或方法的设置调用
 * 组合模式:当整个项目呈现明显的树形结构时可使用,体现局部和整体的关系,例如人员关系表
 * 装饰模式:类似于JDK中的流,子类都实现一个接口或抽象类,同时类中会存在一个构造函数以接口类型为参数
 *          桥接模式是抽象类中声明一个自己的实现类变量,其他所有的子类继承抽象类或实现类
 * 桥接->策略:基本相同,桥接模式只是将执行类换成了一个可变的抽象类.策略模式不关心具体执行的类,只关心执行过程
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
		// 策略模式
		strategy();
		// 桥接模式
		bridge();
		// 组合模式
		composite();
		// 适配器模式
		adapter();
		// 解释器模式
		interpreter();
		// 责任链模式
		chain();
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

	public static void strategy() {
		int lineCount = 4;
		int lineWidth = 12;
		Context myContext = new Context();
		StrategyA strategyA = new StrategyA();
		StrategyB strategyB = new StrategyB();
		String s = "This is a test string ! This is a test string ! This is a test string ! This is a test string !";
		myContext.setText(s);
		myContext.setLineWidth(lineWidth);
		myContext.setStrategy(strategyA);
		myContext.drawText();
		myContext.setLineCount(lineCount);
		myContext.setStrategy(strategyB);
		myContext.drawText();
	}

	public static void bridge() {
		CarGift carGift = new CarGift(new FlowerSend());
		carGift.getGiftSend().price();
	}

	public static void composite() {
		BeautyCartoon beautyCartoon = new BeautyCartoon();
		MyGodness myGodness = new MyGodness();
		beautyCartoon.children().add(myGodness);
		for (TypeCartoon typeCartoon : beautyCartoon.children()) {
			System.out.println(typeCartoon.name());
		}
	}

	public static void adapter() {
		// 委派适配
		Text myText = new Text();
		Adapter adapter = new Adapter(myText);
		adapter.draw();
		adapter.border();
		adapter.setContent("A test text !");
		System.out.println("The content in Text Shape is :" + adapter.getContent());

		// 继承适配
		AdapterA adapterA = new AdapterA();
		adapterA.draw();
		adapterA.border();
		adapterA.setContent("A test text !");
		System.out.println("The content in Text Shape is :" + adapterA.getContent());
	}

	public static void interpreter() {
		String number = "20";
		ContextInterpreter context = new ContextInterpreter(number);

		// Interpreter interpreter1 = new MinusInterpreter();
		// interpreter1.interpret(context);
		// System.out.println(context.getOutput());
		//
		// Interpreter interpreter2 = new PlusExpression();
		// interpreter2.interpret(context);
		// System.out.println(context.getOutput());
		//
		// Interpreter interpreter3 = new PlusInterpreter();
		// interpreter3.interpret(context);
		// System.out.println(context.getOutput());
		//
		// Interpreter interpreter4 = new PlusInterpreter();
		// interpreter4.interpret(context);
		// System.out.println(context.getOutput());

		List<Interpreter> list = new ArrayList<>();
		list.add(new PlusInterpreter());
		list.add(new PlusInterpreter());
		list.add(new MinusInterpreter());
		list.add(new MinusInterpreter());
		list.add(new MinusInterpreter());
		list.add(new MinusInterpreter());

		for (Interpreter ex : list) {
			ex.interpret(context);
			System.out.println(context.getOutput());
		}
	}

	public static void chain() {
		AbstractChain chain1 = new ChainA();
		ChainB chain2 = new ChainB();
		chain1.setNextOne(chain2);
		chain1.handlerMsg(new NeedHandler());
	}
}