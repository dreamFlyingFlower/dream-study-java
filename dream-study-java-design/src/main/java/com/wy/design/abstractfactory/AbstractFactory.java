package com.wy.design.abstractfactory;

/**
 * 抽象工厂模式:接口A->抽象B,需要实现A中所有方法->实现C,需要继承抽象类B,可实现自己的方法
 * @instruction 和工厂区别在于多了一个中间的抽象类,有默认实现.只要是带 抽象类的在我看来都是抽象工厂模式.
 *              即使多个接口,多个抽象类,再通过第3个或第N个接口或抽象类将这些不相干的接口联系起来,也是抽象工厂
 * @author paradiseWy 2018年12月8日
 */
public interface AbstractFactory {

}