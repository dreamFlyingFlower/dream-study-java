package com.wy.netty.chat.client.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wy.netty.chat.ChatType;
import com.wy.netty.chat.client.swing.ResultCodeTip;
import com.wy.netty.chat.client.swing.Swingclient;
import com.wy.netty.chat.model.ResultCode;
import com.wy.netty.chat.response.ChatResponse;

@Component
public class ChatHandlerImpl implements ChatHandler {

	@Autowired
	private Swingclient swingclient;

	@Autowired
	private ResultCodeTip resultCodeTip;

	@Override
	public void publicChat(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			swingclient.getTips().setText("发送成功");
		} else {
			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void privateChat(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			swingclient.getTips().setText("发送成功");
		} else {
			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void receieveMessage(int resultCode, byte[] data) {

		ChatResponse chatResponse = new ChatResponse();
		chatResponse.readFromBytes(data);

		if (chatResponse.getChatType() == ChatType.PUBLIC_CHAT) {
			StringBuilder builder = new StringBuilder();
			builder.append(chatResponse.getSendPlayerName());
			builder.append("[");
			builder.append(chatResponse.getSendPlayerId());
			builder.append("]");
			builder.append(" 说:\n\t");
			builder.append(chatResponse.getMessage());
			builder.append("\n\n");

			swingclient.getChatContext().append(builder.toString());
		} else if (chatResponse.getChatType() == ChatType.PRIVATE_CHAT) {
			StringBuilder builder = new StringBuilder();

			if (swingclient.getPlayerResponse().getPlayerId() == chatResponse.getSendPlayerId()) {
				builder.append("您悄悄对 ");
				builder.append("[");
				builder.append(chatResponse.getTartgetPlayerName());
				builder.append("]");
				builder.append(" 说:\n\t");
				builder.append(chatResponse.getMessage());
				builder.append("\n\n");
			} else {
				builder.append(chatResponse.getSendPlayerName());
				builder.append("[");
				builder.append(chatResponse.getSendPlayerId());
				builder.append("]");
				builder.append(" 悄悄对你说:\n\t");
				builder.append(chatResponse.getMessage());
				builder.append("\n\n");
			}
			swingclient.getChatContext().append(builder.toString());
		}
	}
}