package dream.study.common.qq.server;

import java.net.ServerSocket;
import java.net.Socket;

public class QQServer {

	private ServerSocket ss;

	private int port = 8888;

	public QQServer() {
		try {
			ss = new ServerSocket(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			while (true) {
				Socket sock = ss.accept();
				new ServerReceiverThread(sock).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}