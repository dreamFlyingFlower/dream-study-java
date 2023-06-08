package com.wy.jvm;

/**
 * JDK9之前String底层用字符数组,JDK9之后用字节数组
 * 
 * String在JVM中字符串常量池是一个固定大小的HashTable,在JDK7之前默认是1009,JDK7之后是60013,最小值不能小于1009
 * 使用-XX:StringTableSize可改变字符串常量池大小
 * 
 * JDK7之前字符串常量放在永久代中,JDK7后字符串常量池放在堆中.
 * 
 * String.intern():返回该字符串在常量池中的地址,根据情况不同改变字符串本身的指针指向:
 * 如果该对象本身指向堆,常量池中已经有值,则调用该方法后该字符串对象指针地址不变;
 * 如果该对象本身指向堆,但是常量池没有值,则会在常量池创建该字符串常量,并将该字符串指针指向常量池中地址
 * 
 * 字符串拼接:
 * 
 * <pre>
 * 1.常量与常量的拼接结果在常量池,原理是编译期优化
 * 2.常量池中不会存在相同内容的常量
 * 3.只要其中有一个是变量,结果就在堆中.变量拼接的原理是StringBuilder
 * 4.如果拼接的结果调用intern(),则主动将常量池中还没有的字符串对象放入池中,并返回此对象地址
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-06-08 23:06:10
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyString {

	public static void main(String[] args) {
		String s1 = "a" + "b" + "c"; // 直接编译成abc
		String s2 = "abc";

		System.out.println(s1 == s2); // true

		String s3 = "dream";
		String s4 = "flying";
		String s5 = "dreamflying";
		String s6 = "dream" + "flying";
		// 如果拼接符号的前后出现了变量,则相当于在堆空间中new string(),具体的内容为拼接的结果
		String s7 = s3 + "flying";
		String s8 = "javaEE" + s4;
		// 只要出现变量,在JVM中实际上是先new一个StringBuilder(),然后append,之后再调用StringBuilder.toString方法生成一个String对象
		String s9 = s3 + s4;
		System.out.println(s5 == s6);// true
		System.out.println(s5 == s7);// false
		System.out.println(s5 == s8);// false
		System.out.println(s5 == s9);// false
		String s10 = s9.intern();
		System.out.println(s5 == s10);// true

		// final将变量变成了常量
		final String s11 = "ab";
		final String s12 = "c";
		System.out.println(s2 == s11 + s12); // true

		// 创建了2个对象:一个在堆中的String对象,一个在字符串常量池的ab.根据字节码文件可以看出
		new String("ab");
		// 创建了6个对象:拼接时使用了StringBuilder,创建对象a,b,字面量a,b
		// 最后StringBuilder.toString()生成了一个String对象,但是在常量池中没有对象的字面量
		String s13 = new String("a") + new String("b");
		System.out.println(s13);

		String s14 = new String("1");
		s14.intern();// 调用此方法之前,字符串常量池中已经存在了1,此时s14仍指向堆中的1
		String s15 = s14.intern();// 此时会将常量池的地址返回给s15,但s14仍指向堆
		String s16 = "1";
		System.out.println(s14 == s16);// jdk6: falsejdk7/8: false
		System.out.println(s15 == s16);// true
		String s17 = new String("1") + new String("1");// s16变量记录的地址为: new String("11"),字符串常量池中不存在11
		s17.intern();// 在字符串常量池中生成11,并指向常量池地址
		String s18 = "11";// s17变量记录的地址:使用的是上一行代码代码行时,在常量中生成的11的地址
		System.out.println(s17 == s18);// jdk6: false jdk7/8: true
	}
}