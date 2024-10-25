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

	@Override
	public void name() {
		System.out.println("son");
	}

	public static void name1() {
		System.out.println("son");
	}
}