package com.wy.jvm;

/**
 * 字节码文件解读
 * 
 * @author 飞花梦影
 * @date 2021-09-11 16:45:03
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyClass {

	public static int add(int a, int b) {
		int hour = 24;
		long m1 = hour * 60 * 60 * 1000;
		long m2 = hour * 60 * 60 * 1000 * 1000;
		// 结果是500654080
		// 在内存中计算时由于都是int类型,计算的结果也是int类型,但超出了int类型的最大值
		// 根据2进制int类型的长度,只会获得最终bit位的后32位,前面超出的舍弃,最后获得是500654080,得出的结果才会转换为long
		// 可以使用javap -verbose 该类的字节码文件,查看运行指令
		System.out.println((int) m2);
		// 5
		System.out.println(m2 / m1);
		return 1 + 1;
	}

	@SuppressWarnings("unused")
	public void test() {
		// 在字节码文件中,s和i公用一个槽位,因为出了各自的作用域
		{
			int i = 9;
		}
		{
			String s = "xxx";
		}

		int i = 10;
		double j = i / 0.0;
		System.out.println(j); // Infinity,无穷大

		double m = 10.0;
		double n = m / 0.0;
		System.out.println(n); // NaN
	}

	public static void main(String[] args) {
		add(1, 2);
	}
}