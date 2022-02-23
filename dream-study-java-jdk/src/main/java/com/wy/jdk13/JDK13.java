package com.wy.jdk13;

/**
 * JDK13新特性
 * 
 * <pre>
 * 1.swtich-case的case中可以使用{}写复杂的逻辑,使用yield返回值
 * 2.文本块的支持.类似Python中的"""内容""",可以在"""中进行换行操作,而且输出是原样输出
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-06-25 10:30:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JDK13 {

	public static void main(String[] args) {
		int ret = switch (key) {
		case 1 -> 1;
		case 2 -> {
			if (key == 2) {
				yield 2*2;
			}else {
				yield 1*2;
			}
		}
		default -> 0;
		}
	}
}