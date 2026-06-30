package com.wy.config;

import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphDefinition;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.langgraph.ChatGraphState;
import com.wy.langgraph.CustomizeNode;

import lombok.RequiredArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-06-29 15:02:01
 */
@Configuration
@RequiredArgsConstructor
public class LangGraphConfig {

	private CustomizeNode customizeNode;

	@Bean
	CompiledGraph<ChatGraphState> myGraph() throws Exception {
		return new StateGraph<>(ChatGraphState.SCHEMA, (initData) -> new ChatGraphState(initData))
				.addNode("customize", customizeNode) // 将节点加入图中
				.addEdge(GraphDefinition.START, "customize") // 定义流转边
				.addEdge("customize", GraphDefinition.END)
				.compile(); // 编译生成不可变图
	}
}