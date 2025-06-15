package dream.study.common.extend;

/**
 * 子类
 * 
 * @author 飞花梦影
 * @date 2020-09-29 09:45:15
 */
public class Son extends Parent {

	public int richer = 500;

	public static int boy = 1;

	public static int j = method();

	public int n = method2();

	static {
		System.out.println(6);
	}

	public Son() {
		System.out.println(7);
	}

	static {
		System.out.println(11);
	}

	{
		System.out.println(8);
	}

	@Override
	public void name() {
		System.out.println(9);
		System.out.println("son");
	}

	public static void name1() {
		System.out.println("son");
	}

	public static int method() {
		System.out.println(10);
		return 1;
	}

	@Override
	public int method2() {
		System.out.println(15);
		return 1;
	}
}