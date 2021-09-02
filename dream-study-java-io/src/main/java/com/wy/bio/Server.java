package com.wy.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO服务端
 * 
 * @author 飞花梦影
 * @date 2021-09-02 14:40:39
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Server {

	static ExecutorService service = Executors.newFixedThreadPool(50);

	public static void main(String[] args) {
		int port = genPort(args);
		try (ServerSocket server = new ServerSocket(port);) {
			System.out.println("server started!");
			while (true) {
				Socket socket = server.accept();
				service.execute(new Handler(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class Handler implements Runnable {

		Socket socket = null;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try (BufferedReader reader =
					new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
					PrintWriter writer = new PrintWriter(
							new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));) {
				String readMessage = null;
				while (true) {
					System.out.println("server reading... ");
					if ((readMessage = reader.readLine()) == null) {
						break;
					}
					System.out.println(readMessage);
					writer.println("server recive : " + readMessage);
					writer.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				socket = null;
			}
		}
	}

	private static int genPort(String[] args) {
		if (args.length > 0) {
			try {
				return Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				return 9999;
			}
		} else {
			return 9999;
		}
	}
}