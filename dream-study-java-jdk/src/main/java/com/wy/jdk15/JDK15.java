package com.wy.jdk15;

/**
 * JDK15新特性
 * 
 * <pre>
 * 1.主要是对前2个版本功能的深化,稳定
 * 2.sealed限制类和接口继承,permits关键字指定继承或实现的类,接口.no-sealed非密封类可以被所有类继承
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2022-02-24 20:19:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public sealed class JDK15 permits Test01,Test02,Test05{

}

// 继承密封类的类需要被申明为final或密封类或非密封类
final class Test01 extends JDK15{}

sealed class Test02 extends JDK15 permits Test04{}

no-sealed class Test05 extends JDK15 {}

// 错误,无法继承
//class Test03 extends JDK15{}

class Test04 extends Test02{}