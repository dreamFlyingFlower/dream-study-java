package dream.study.common.extend;

/**
 * 父接口
 * 
 * @author 飞花梦影
 * @date 2020-09-29 10:02:03
 */
public interface IParent {

	int richer = 500000;

	static void name() {
		System.out.println("iparent static func");
	}

	default void name1() {
		System.out.println("iparent default name1");
	}
}