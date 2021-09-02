package com.wy.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * BIO客户端
 * 
 * @author 飞花梦影
 * @date 2021-09-02 14:40:49
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Client {

	public static void main(String[] args) {
		String host = null;
		int port = 0;
		if (args.length > 2) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			host = "127.0.0.1";
			port = 9999;
		}

		try (Scanner s = new Scanner(System.in);
				Socket socket = new Socket(host, port);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);) {
			String message = null;
			while (true) {
				message = s.nextLine();
				if (message.equals("exit")) {
					break;
				}
				writer.println(message);
				writer.flush();
				System.out.println(reader.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}