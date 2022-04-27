package com.wy.jdk17;

/**
 * 1.可使用 {@link HexFormat} 对字节数据进行操作
 *
 * @author 飞花梦影
 * @date 2022-04-27 14:29:05
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class JDK17 {

	public static void main(String[] args) {
		HexFormat format = HexFormat.of();

		byte[] input = new byte[] { 127, 0, -50, 105 };
		String hex = format.formatHex(input);
		System.out.println(hex);

		byte[] output = format.parseHex(hex);
		assert Arrays.compare(input, output) == 0;
	}
}