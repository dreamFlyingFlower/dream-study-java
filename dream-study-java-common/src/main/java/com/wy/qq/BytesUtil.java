package com.wy.qq;

/**
 * 字节工具类型
 * 
 * @author ParadiseWY
 * @date 2020-11-16 16:14:41
 * @git {@link https://github.com/mygodness100}
 */
public class BytesUtil {

	/**
	 * 将整数转换成字节数组
	 */
	public static byte[] int2ByteArr(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (i >> 0);
		bytes[1] = (byte) (i >> 8);
		bytes[2] = (byte) (i >> 16);
		bytes[3] = (byte) (i >> 24);
		return bytes;
	}

	/**
	 * 将整数转换成字节数组
	 */
	public static byte[] long2ByteArr(long i) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) (i >> 0);
		bytes[1] = (byte) (i >> 8);
		bytes[2] = (byte) (i >> 16);
		bytes[3] = (byte) (i >> 24);
		bytes[4] = (byte) (i >> 32);
		bytes[5] = (byte) (i >> 40);
		bytes[6] = (byte) (i >> 48);
		bytes[7] = (byte) (i >> 56);
		return bytes;
	}

	/**
	 * 将字节数组转换成整数
	 */
	public static int byteArr2Int(byte[] arr) {
		return ((arr[0] & 0xFF) << 0) | ((arr[1] & 0xFF) << 8) | ((arr[2] & 0xFF) << 16) | ((arr[3] & 0xFF) << 24);
	}

	/**
	 * 将字节数组转换成整数
	 */
	public static long byteArr2Long(byte[] arr) {
		return ((arr[0] & 0xFFL) << 0) | ((arr[1] & 0xFFL) << 8) | ((arr[2] & 0xFFL) << 16) | ((arr[3] & 0xFFL) << 24)
				| ((arr[4] & 0xFFL) << 32) | ((arr[5] & 0xFFL) << 40) | ((arr[6] & 0xFFL) << 48)
				| ((arr[7] & 0xFFL) << 56);
	}
}