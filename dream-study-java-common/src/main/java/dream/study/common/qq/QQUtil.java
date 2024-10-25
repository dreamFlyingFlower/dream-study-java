package dream.study.common.qq;

import java.net.Socket;

/**
 * 字节工具类型
 */
public class QQUtil {

	/**
	 * 从socket得到远端的地址信息
	 */
	public static String getRemoteAddressString(Socket sock) {
		String ip = sock.getInetAddress().getHostAddress();
		int port = sock.getPort();
		return ip + ":" + port;
	}

	/**
	 * 从socket得到远端的地址信息
	 */
	public static byte[] getRemoteAddressBytes(Socket sock) {
		return getRemoteAddressString(sock).getBytes();
	}
}