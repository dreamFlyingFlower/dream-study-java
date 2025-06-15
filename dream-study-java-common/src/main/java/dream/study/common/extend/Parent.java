package dream.study.common.extend;

/**
 * 父类
 * 
 * @author 飞花梦影
 * @date 2020-09-29 09:53:08
 */
public class Parent {

	public int richer = 500000;

	public static int boy = 10;

	public static final int m = method1();

	public static int j = method();

	public int n = method2();

	static {
		System.out.println(1);
	}

	public Parent() {
		System.out.println(2);
	}

	{
		System.out.println(3);
	}
	static {
		System.out.println(12);
	}

	public void name() {
		System.out.println(4);
		System.out.println("parent");
	}

	public static void name1() {
		System.out.println("parent");
	}

	public static int method() {
		System.out.println(5);
		return 1;
	}

	public static int method1() {
		System.out.println(13);
		return 1;
	}

	public int method2() {
		System.out.println(14);
		return 1;
	}
}