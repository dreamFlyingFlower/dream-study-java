package dream.study.common.ex;

/**
 * 枚举的例子
 * 1.枚举中所有的属性都是public static final
 * 2.枚举中的构造都是私有属性
 * 3.构造函数必须和属性所带的参数类型和格式相同
 * @author wanyang 2018年7月8日
 */
public enum S_Enum {
	MONDAY,TUESDAY,MONDAY1("星期一"),TUESDAY1("星期二");
	
	private String name;
	
	private S_Enum() {
		
	}
	
	private S_Enum(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public static void getOr() {
		System.out.println("static");
	}
}