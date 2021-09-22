package com.wy.netty.chat.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wy.netty.chat.exception.ErrorCodeException;
import com.wy.netty.chat.model.Result;
import com.wy.netty.chat.model.ResultCode;
import com.wy.netty.chat.request.PrivateChatRequest;
import com.wy.netty.chat.request.PublicChatRequest;
import com.wy.netty.chat.server.service.ChatService;

@Component
public class ChatHandlerImpl implements ChatHandler {

	@Autowired
	private ChatService chatService;

	@Override
	public Result<?> publicChat(long playerId, byte[] data) {
		try {
			// 反序列化
			PublicChatRequest request = new PublicChatRequest();
			request.readFromBytes(data);

			// 参数校验
			if (StringUtils.hasText(request.getContext())) {
				return Result.ERROR(ResultCode.AGRUMENT_ERROR);
			}

			// 执行业务
			chatService.publicChat(playerId, request.getContext());
		} catch (ErrorCodeException e) {
			return Result.ERROR(e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return Result.ERROR(ResultCode.UNKOWN_EXCEPTION);
		}
		return Result.SUCCESS();
	}

	@Override
	public Result<?> privateChat(long playerId, byte[] data) {
		try {
			// 反序列化
			PrivateChatRequest request = new PrivateChatRequest();
			request.readFromBytes(data);

			// 参数校验
			if (StringUtils.hasText(request.getContext()) || request.getTargetPlayerId() <= 0) {
				return Result.ERROR(ResultCode.AGRUMENT_ERROR);
			}

			// 执行业务
			chatService.privateChat(playerId, request.getTargetPlayerId(), request.getContext());
		} catch (ErrorCodeException e) {
			return Result.ERROR(e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return Result.ERROR(ResultCode.UNKOWN_EXCEPTION);
		}
		return Result.SUCCESS();
	}
}