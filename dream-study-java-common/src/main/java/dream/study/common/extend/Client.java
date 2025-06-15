package dream.study.common.extend;

/**
 * 继承和实现
 * 
 * 非静态->同名属性,只根据声明的类来取值,接口也一样;同名方法则看实体对象的指向,接口也一样
 * 
 * 静态->同名属性和同名方法,只根据声明的类来取值,接口也一样
 * 
 * 初始化过程:
 * 
 * <pre>
 * 按定义顺序加载父类静态变量,常量->按定义顺序加载父类静态代码块->按定义顺序加载子类常量,静态变量->按定义顺序加载子类静态代码块
 * 
 * 按定义顺序加载父类全局变量->按定义顺序加载父类代码块->父类构造->按定义顺序加载子类全部变量->按定义顺序加载子类代码块->子类构造
 * 
 *	先加载静态变量,常量,再加载全局变量;先加载父类,再加载子类
 *
 * 静态变量,常量:不管是静态还是常量,如果都是利用方法初始化,则按照变量定义顺序加载.静态变量和常量只会加载一次
 * 
 * 全局变量:先初始化父类,再初始化子类.如果子类覆盖了父类方法,调用的都是子类方法,变量会加载2次
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-09-29 09:45:45
 */
public class Client {

	public static void main(String[] args) {
		test2();
	}

	public static void test1() {
		Parent parent = new Son();//
		System.out.println(parent.richer); // 500000
		System.out.println(((Parent) parent).richer);// 500000
		// System.out.println(parent.boy); // 10
		// System.out.println(((Parent) parent).boy); // 10
		parent.name(); // son
		// parent.name1(); // parent1
		((Parent) parent).name(); // son
		// ((Parent) parent).name1(); // parent1
	}

	public static void test2() {
		Son son = new Son();
		System.out.println(son.richer); // 500
		System.out.println(((Parent) son).richer);// 500000
		son.name(); // son
		// son.name1(); // son1
		((Parent) son).name(); // son
		// ((Parent) son).name1(); // parent1
		Son son1 = new Son();
		System.out.println(son1);
	}

	public static void test3() {
		IParent iParent = new ISon();
		// System.out.println(iParent.richer); // 500000
		// System.out.println(((IParent)iParent).richer); // 500000
		IParent.name(); // iparent static func
		iParent.name1(); // ison default name1
	}

	public static void test4() {
		ISon iSon = new ISon();
		// System.out.println(iSon.richer); // 50
		// System.out.println(((IParent)iSon).richer); // 500000
		// iSon.name(); // iparent static func
		// iSon.name1(); // ison default name1
		((IParent) iSon).name1(); // ison default name1
	}

	public static void test5() {
		// 先调父类构造,父类构造调用了this.name(),该方法被子类重写,调用子类name(),此时子类构造还未调用完成,成员变量未初始化,只有默认值,先输出0
		// 父类构造完成后调用子类构造,给成员变量赋值.构造完成,再次调用子类name(),此时richer值为500
		Parent1 parent1 = new Son1(); // 0,500
		System.out.println(parent1.richer); // 1000000
	}
}