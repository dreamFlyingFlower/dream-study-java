package com.wy.langgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import dev.langchain4j.data.message.ChatMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 自定义状态类: 在节点间传递上下文、消息、工具返回结果
 *
 * @author 飞花梦影
 * @date 2026-06-29 14:11:49
 */
@Getter
@Setter
@ToString
public class ChatGraphState extends AgentState {

	public static final String MESSAGES_KEY = "messages";

	public static final Map<String, Channel<?>> SCHEMA = Map.of(MESSAGES_KEY, Channels.appender(ArrayList::new));

	public ChatGraphState(Map<String, Object> initData) {
		super(initData);
	}

	@SuppressWarnings("unchecked")
	public List<ChatMessage> getMessages() {
		return (List<ChatMessage>) this.value(MESSAGES_KEY).orElse(List.of());
	}
}