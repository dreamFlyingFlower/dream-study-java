package com.wy;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Ollama大模型的使用:进入官网：https://ollama.com/,载、安装、启动 ollama
 * 
 * <pre>
 * 1.下载完成之后,打开安装程序,并完成安装步骤
 * 2. 打开终端,执行命令:ollama run llama3.1,等待模型下载完成
 * 3.完成模型下载,会弹出提示:Send a message,此时可以与AI进行互动了
 * </pre>
 * 
 * 当前使用的jar是group.springframework.ai下的,spring.ai也有,但暂时无法从Maven仓库获取,spring的是org.springframework.ai
 *
 * @author 飞花梦影
 * @date 2024-10-24 11:23:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SpringBootTest
class TestOllama {

	@Autowired
	private OllamaChatModel chatModel;

	@Test
	void ollamaChat() {

		ChatResponse response = chatModel.call(new Prompt("Spring Boot适合做什么？",
				OllamaOptions.create().withModel(OllamaModel.LLAMA3.getModelName()).withTemperature(0.4F)));
		System.out.println(response);

	}
}