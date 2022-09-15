package com.wy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wy.abstracfactory.AbstractChinaCartoonFactory;
import com.wy.abstracfactory.AbstractFactoryCartoon;
import com.wy.abstracfactory.AbstractJapanCartoonFactory;
import com.wy.adapter.AdapterExtend;
import com.wy.adapter.AdapterObject;
import com.wy.adapter.Computer;
import com.wy.adapter.SDCard;
import com.wy.adapter.SDCardImpl;
import com.wy.adapter.TFCard;
import com.wy.adapter.TFCardImpl;
import com.wy.bridge.CarGift;
import com.wy.bridge.FlowerSend;
import com.wy.build.HeavenHandler;
import com.wy.build.HubeiHeaven;
import com.wy.chain.AbstractChain;
import com.wy.chain.ChainA;
import com.wy.chain.ChainB;
import com.wy.chain.NeedHandler;
import com.wy.command.CartoonCommand;
import com.wy.command.Command;
import com.wy.command.ContextCommand;
import com.wy.command.TravelCommand;
import com.wy.command.YoungMan;
import com.wy.composite.BeautyCartoon;
import com.wy.composite.MyGodness;
import com.wy.composite.TypeCartoon;
import com.wy.decorator.Animal;
import com.wy.decorator.AnimalLand;
import com.wy.decorator.FlyAnimalDecorator;
import com.wy.decorator.WaterAnimalDecorator;
import com.wy.entity.Cartoon;
import com.wy.entity.StrollSkyJiuGe;
import com.wy.factory.FactoryAir;
import com.wy.factory.FactoryCartoon;
import com.wy.interpreter.ContextInterpreter;
import com.wy.interpreter.Interpreter;
import com.wy.interpreter.MinusInterpreter;
import com.wy.interpreter.PlusInterpreter;
import com.wy.mediator.Mediator;
import com.wy.mediator.MediatorMan;
import com.wy.mediator.MediatorPerson;
import com.wy.mediator.MediatorWoman;
import com.wy.memento.Caretaker;
import com.wy.memento.Godness;
import com.wy.state.JadeDynasty;
import com.wy.strategy.Context;
import com.wy.strategy.StrategyA;
import com.wy.strategy.StrategyB;
import com.wy.template.AbstractTemplate;
import com.wy.template.TemplateA;
import com.wy.visitor.Park;
import com.wy.visitor.VisitorA;
import com.wy.visitor.VisitorB;
import com.wy.visitor.VisitorPark;

/**
 * 24种设计模式
 * 
 * 设计原则:
 * 
 * <pre>
 * 单一职责:单个模块只负责单一类型的操作,即实现类实现多个接口,每个接口尽可能少的定义方法
 * 开放封闭:开放扩展,封闭修改
 * 里氏代换:即将子类替换成父类,程序不会发生改变
 * 依赖倒转:高层模块和最底层的实现类都应该依赖于抽象,不能直接让高层模块使用最底层的实现类
 * 迪米特法则:最少知识原则,即一个对象应当对其他对象尽可能少的了解,类之间的交互可以通过第三者完成
 * </pre>
 * 
 * 设计模式简介:
 * 
 * <pre>
 * 迭代模式:直接可以使用JDK自带的{@link Iterator},{@link Iterable}.主要是为了查找和其他操作的分离
 * 简单工厂模式:适用于简单的创建同一个接口的不同实现类的场景,主要是对象创建
 * 工厂模式:比简单工厂多了个创建接口的接口
 * 享元模式:实际上就是利用Map进行对象缓存
 * 单例模式:所有使用该类的地方都是同一个对象,相当于一个静态类,需要注意属性的共享
 * 原型模式:从一个对象中复制多个其他对象,基础属性相同,多个对象之间的改变互不影响
 * 备忘录模式:用来对对象进行还原.和原型模式差不多,只是用途不一样
 * 策略模式:主要是算法的封装,注重的是方法的实现,而不是扩展
 * 装饰模式:注重的是解耦以及扩展原接口方法,扩展的方法会暴露出来
 * 代理模式:同装饰模式相同,但是代理模式扩展的方法不会暴露,主要操作对象由代理类创建,而不是传递的参数
 * 
 * 简单工厂,工厂,抽象工厂:直接使用工厂或抽象工厂,工厂模式中需要多个抽象的实现时,自动升级为抽象工厂模式
 * </pre>
 * 
 * 设计模式的区别:
 * 
 * <pre>
 * 简单工厂->工厂:简单工厂只有一个接口,直接返回实例;工厂有2个接口,一个返回实例,一个返回实例工厂,通过实例工厂返回实例
 * 简单工厂->建造模式:简单工厂注重创建对象,而建造模式则是接口中多个属性或方法的设置调用
 * 简单工厂->抽象工厂:区别在于抽象工厂创建的对象之间有联系,是一个产品簇;简单工厂创建的对象之间没有关系
 * 组合模式:当整个项目呈现明显的树形结构时可使用,体现局部和整体的关系,例如人员关系表
 * 装饰->桥接:所有子类以及装饰类都实现了接口,装饰类持有接口的实例,接口的方法会在装饰器中被调用,同时装饰器可以增强实现;
 *          桥接模式将使用和实现分开,使用类不实现接口,但是同样持有接口的实例,使用类可以自定义实现
 * 桥接->策略:基本相同,桥接模式只是将执行类换成了一个可变的抽象类.策略模式不关心具体执行的类,只关心执行过程
 * 工厂->策略:工厂注重对象的创建,而策略是注重行为的实现
 * 工厂->抽象工厂:抽象工厂多了中间的抽象类,对接口中的一些方法进行默认的实现
 * 策略->代理:策略模式的使用类不需要实现接口,而代理模式的实用类需要实现接口
 * 策略->门面:策略注重于单个方法的实现,而不是一系列方法的顺序实现,门面则是一系列特定操作的实现
 * 门面->模版:门面是多个对象组合使用完成某一个功能,不同实现由不同的组合对象完成;
 * 		模板模式是父子类继承关系,不同实现由不同子类对象完成;
 * 装饰->策略:策略模式不实现抽象,侧重点不同,其他一样
 * 策略->状态:策略模式注重共同行为的不同实现;状态模式注重各种状态之间的扭转以及共同行为的不同实现
 * 中介和门面:中介是多个对象之间的交互;门面只有外部系统访问,内部系统不访问外部系统
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
		// 模板模式
		template();
		// 备忘录模式
		memento();
		// 状态模式
		state();
		// 命令模式
		command();
		// 中介者模式
		mediator();
		// 访问者模式
		visitor();
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
		StrollSkyJiuGe build = headler.build(hubeiHeaven);
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
		// 调用继承方式适配器
		Computer computer = new Computer();
		SDCard sdCard = new SDCardImpl();
		System.out.println(computer.read(sdCard));

		System.out.println("------------");

		// 调用类适配器
		AdapterExtend adapter = new AdapterExtend();
		System.out.println(computer.read(adapter));

		System.out.println("------------");

		// 调用对象适配器
		TFCard tfCard = new TFCardImpl();
		AdapterObject adapterObject = new AdapterObject(tfCard);
		System.out.println(computer.read(adapterObject));
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

	public static void template() {
		AbstractTemplate template = new TemplateA();
		template.run();
	}

	public static void memento() {
		Godness per = new Godness("飞仙剑影", "女", 24);
		Caretaker<Godness> caretaker = new Caretaker<>();
		caretaker.setMemento(per.clone());
		per.display();
		per.setName("beifeng");
		per.setSex("f");
		per.setAge(1);
		per.display();
		per.setMemento(caretaker.getMemento());
		per.display();
	}

	public static void state() {
		JadeDynasty jadeDynasty = new JadeDynasty();
		jadeDynasty.setAge(7);
		jadeDynasty.doSomething();
		jadeDynasty.setAge(12);
		jadeDynasty.doSomething();
		jadeDynasty.setAge(18);
		jadeDynasty.doSomething();
		jadeDynasty.setAge(8);
		jadeDynasty.doSomething();
		jadeDynasty.setAge(7);
		jadeDynasty.doSomething();
		jadeDynasty.setAge(18);
		jadeDynasty.doSomething();
	}

	public static void command() {
		YoungMan peddler = new YoungMan();
		Command cartoonCommand = new CartoonCommand(peddler);
		Command travelCommand = new TravelCommand(peddler);
		ContextCommand contextCommand = new ContextCommand();
		contextCommand.setCommand(cartoonCommand);
		contextCommand.setCommand(travelCommand);
		contextCommand.removeCommand(travelCommand);
		contextCommand.enjoy();
	}

	public static void mediator() {
		Mediator mediator = new Mediator();
		MediatorPerson zhangsan = new MediatorMan("张三", 7, mediator);
		MediatorPerson lisi = new MediatorMan("李四", 7, mediator);
		MediatorPerson xiaofang = new MediatorWoman("小芳", 7, mediator);
		zhangsan.getPartner(lisi);
		xiaofang.getPartner(lisi);
	}

	public static void visitor() {
		Park park = new Park();
		park.setName("越秀公园");
		VisitorA visitorA = new VisitorA();
		park.accept(visitorA);
		VisitorB visitorB = new VisitorB();
		park.accept(visitorB);
		VisitorPark visitorManager = new VisitorPark();
		park.accept(visitorManager);
	}
}