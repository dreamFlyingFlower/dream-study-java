package com.wy.netty.chat.client.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wy.netty.chat.client.swing.ResultCodeTip;
import com.wy.netty.chat.client.swing.Swingclient;
import com.wy.netty.chat.model.ResultCode;
import com.wy.netty.chat.response.PlayerResponse;

/**
 * 玩家模块
 */
@Component
public class PlayerHandlerImpl implements PlayerHandler {

	@Autowired
	private Swingclient swingclient;

	@Autowired
	private ResultCodeTip resultCodeTip;

	@Override
	public void registerAndLogin(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			PlayerResponse playerResponse = new PlayerResponse();
			playerResponse.readFromBytes(data);

			swingclient.setPlayerResponse(playerResponse);
			swingclient.getTips().setText("注册登录成功");
		} else {
			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}

	@Override
	public void login(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			PlayerResponse playerResponse = new PlayerResponse();
			playerResponse.readFromBytes(data);

			swingclient.setPlayerResponse(playerResponse);
			swingclient.getTips().setText("登录成功");
		} else {
			swingclient.getTips().setText(resultCodeTip.getTipContent(resultCode));
		}
	}
}