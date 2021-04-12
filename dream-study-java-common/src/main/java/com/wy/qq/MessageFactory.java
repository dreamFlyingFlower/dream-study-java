package com.wy.qq;

import java.io.InputStream;
import java.net.Socket;

/**
 * 消息工厂
 */
public class MessageFactory {

	/**
	 * 服务器端解析client消息,并生成服务器端消息
	 */
	public static Message parseClientMessageAndGenServerMsg(Socket sock) {
		try {
			//
			InputStream in = sock.getInputStream();
			// 读取消息类型
			int msgType = in.read();
			switch (msgType) {
			// 刷新列表
			case Message.CLIENT_TO_SERVER_REFRESHFRIENDLIST:
				return new ServerRefreshFriendListMessage();

			// 群聊
			case Message.CLIENT_TO_SERVER_CHATS: {
				// 读取4个字节的消息长度
				byte[] bytes4 = new byte[4];
				in.read(bytes4);
				int msgLength = BytesUtil.byteArr2Int(bytes4);

				// 读取消息
				byte[] msg = new byte[msgLength];
				in.read(msg);

				// 得到发送方地址
				String senderAddr = QQUtil.getRemoteAddressString(sock);
				return new ServerChatsMessage(senderAddr, msg);
			}
			// 私聊
			case Message.CLIENT_TO_SERVER_CHAT: {
				// 读取4个字节的消息长度
				byte[] bytes4 = new byte[4];
				in.read(bytes4);

				// 接受方地址长度
				int recvAddrLength = BytesUtil.byteArr2Int(bytes4);

				// 接受方地址
				byte[] recvAddr = new byte[recvAddrLength];
				in.read(recvAddr);

				// 发送方消息长度
				byte[] msgLengthBytes = new byte[4];
				in.read(msgLengthBytes);

				// 消息长度
				int msglength = BytesUtil.byteArr2Int(msgLengthBytes);

				// 消息
				byte[] msgBytes = new byte[msglength];
				in.read(msgBytes);
				return new ServerChatMessage(recvAddr, QQUtil.getRemoteAddressBytes(sock), msgBytes);
			}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}