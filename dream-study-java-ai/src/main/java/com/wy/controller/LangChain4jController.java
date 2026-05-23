package com.wy.controller;

import java.nio.file.Path;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentLoader;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

/**
 * LangChain4j
 * 
 * LangChain4j 的核心其实就七件事：模型、AI Services、记忆、工具、检索增强、流式、结构化输出
 * 
 * <pre>
 * {@link ChatLanguageModel}: 所有对话的起点,给AI消息,AI回复消息.所有模型提供商都只需要实现这一个接口
 *		{@link dev.langchain4j.data.message.SystemMessage}: 设定角色、设定约束
 *		{@link dev.langchain4j.data.message.UserMessage}: 用户输入
 *		{@link AiMessage}: 模型回复
 *		{@link ToolExecutionResultMessage}: 工具执行结果
 *	{@link ChatMemory}: 模型上下文.LLM 本身是无状态的,每一次调用都是独立的.想让它记得上一轮说过什么,就必须把历史消息再塞回去
 *
 *	{@link ChatMemoryStore}: 将历史消息落到 Redis、MySQL 或数据库,这样应用重启、用户切机都不会丢上下文
 *
 * </pre>
 * 
 * LangChain4j 把 RAG 拆成了几个高度解耦的接口:
 * 
 * <pre>
 * {@link DocumentLoader}: 从文件、URL、数据库加载原始文档
 * {@link DocumentSplitter}: 切块(按段落、按句子、按 token)
 * {@link EmbeddingModel}: 把文本变成向量
 * {@link EmbeddingStore}: 向量库(Pinecone、Milvus、PgVector、Elasticsearch…)
 * {@link ContentRetriever}: 把以上组合起来,对外提供"检索"这一件事
 * </pre>
 *
 * @author 飞花梦影
 * @date 2026-05-23 09:45:14
 */
@RestController
@RequestMapping("lang-chain")
public class LangChain4jController {

	public void test1() {
		ChatLanguageModel model =
				OpenAiChatModel.builder().apiKey(System.getenv("OPENAI_API_KEY")).modelName("gpt-4o-mini").build();

		String answer = model.generate("用一句话解释什么是 LangChain4j？");
		System.out.println(answer);
	}

	public void test2() {

		ChatLanguageModel model = OpenAiChatModel.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();

		Translator translator = AiServices.create(Translator.class, model);

		System.out.println(translator.translate("LangChain4j makes building LLM apps feel native to Java."));
	}

	public void test3() {
		ChatLanguageModel model = OpenAiChatModel.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();

		ArticleAssistant articleAssistant = AiServices.create(ArticleAssistant.class, model);

		System.out.println(articleAssistant.writeIntro("曲速引擎", "小学生"));
	}

	public void test4() {
		// 滑动窗口策略: 取最近的20条数据
		MessageWindowChatMemory.withMaxMessages(20);
		// 按 token 预算裁剪更精准,Tokenizer根据使用的模型供应商不同而不同
		TokenWindowChatMemory.withMaxTokens(100_0000, new OpenAiTokenizer("module name"));

		ChatLanguageModel model = OpenAiChatModel.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();

		Assistant assistant = AiServices.builder(Assistant.class)
				.chatLanguageModel(model)
				.chatMemoryProvider(userId -> MessageWindowChatMemory.withMaxMessages(20))
				.build();
		assistant.chat("1", "我上21句说了什么?");
	}

	public void testRag() {
		// 1.用户提问
		// 2.对问题做Embedding
		// 3.在向量库中相似度检索
		// 4.取Top-K相关片段
		// 5.与原问题一起拼成prompt
		// 6.调用LLM生成答案
		// 7.将答案返回给用户
		EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
		EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

		Document document = FileSystemDocumentLoader.loadDocument(Path.of("docs/company-handbook.pdf"),
				new ApachePdfBoxDocumentParser());

		DocumentSplitter splitter = DocumentSplitters.recursive(500, 50);
		List<TextSegment> segments = splitter.split(document);

		List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
		embeddingStore.addAll(embeddings, segments);

		ContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
				.embeddingModel(embeddingModel)
				.embeddingStore(embeddingStore)
				.maxResults(5)
				.minScore(0.6)
				.build();
		ChatLanguageModel model = OpenAiChatModel.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();

		AssistantCompany assistant =
				AiServices.builder(AssistantCompany.class).chatLanguageModel(model).contentRetriever(retriever).build();

		String answer = assistant.chat("公司年假政策是怎样的？");
		System.out.println(answer);
	}

	public void test5() {
		// StreamingChatLanguageModel:输出效果为一个字一个字往外蹦,在 Web 场景下,配合 Spring WebFlux / SSE
		// 就能把 token 实时推到前端
		// AI Services 也支持流式——把方法返回值声明成 TokenStream 即可，写法保持一致
		StreamingChatLanguageModel streamingModel = OpenAiStreamingChatModel.builder()
				.apiKey(System.getenv("OPENAI_API_KEY"))
				.modelName("gpt-4o-mini")
				.build();

		streamingModel.generate("讲一个 100 字的程序员笑话", new StreamingResponseHandler<AiMessage>() {

			@Override
			public void onNext(String token) {
				System.out.print(token);
			}

			@Override
			public void onComplete(Response<AiMessage> resp) {
				System.out.println("\n[END]");
			}

			@Override
			public void onError(Throwable error) {
				error.printStackTrace();
			}
		});
	}

	/**
	 * 结构化输出: 把字符串转成对象
	 * 
	 * 模型返回的是自然语言,业务系统需要结构化数据,LangChain4j 会根据 AI Service 方法的返回类型,自动把模型的回复解析为目标 POJO
	 * 
	 * 1.框架会把目标类型的 JSON Schema 以"请严格按这个格式返回"的方式写进 Prompt
	 * 
	 * 2.收到回复后自动反序列化为 Resume 对象。失败时还能触发一次 LLM 自修复
	 */
	public void test6() {

		record Resume(String name, int age, List<String> skills, String summary) {
		}

		interface ResumeParser {

			@UserMessage("请从下面这段简历文本中提取结构化信息：\n{{it}}")
			Resume parse(String resumeText);
		}
		ChatLanguageModel model = OpenAiChatModel.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();

		Resume r = AiServices.create(ResumeParser.class, model).parse("李雷，29 岁，掌握 Java / Spring / Kafka，负责过……");

		System.out.println(r);
	}
}

interface Translator {

	/**
	 * {@link SystemMessage}: 设定角色、设定约束
	 * 
	 * {@link UserMessage}: 接收用户传入消息
	 * 
	 * @param text 用户传入消息
	 * @return
	 */
	@SystemMessage("你是一位资深的中英互译专家，翻译力求准确、自然、避免直译腔。")
	String translate(@UserMessage String text);
}

interface ArticleAssistant {

	/**
	 * 方法入参通过 @V 绑定到 Prompt 模板的变量,框架拼好最终文本
	 * 
	 * @param keywords
	 * @param audience
	 * @return
	 */
	@SystemMessage("你是一位科技博客的主编，擅长把复杂概念讲成故事。")
	@UserMessage("""
			请根据下面的关键词写一段不少于 200 字的引言：
			关键词：{{keywords}}
			目标读者：{{audience}}
			""")
	String writeIntro(@V("keywords") String keywords, @V("audience") String audience);
}

interface Assistant {

	/**
	 * {@link MemoryId}: 区分不同用户 / 会话,每个 ID 对应一条独立记忆
	 */
	String chat(@MemoryId String userId, @UserMessage String message);
}

interface AssistantCompany {

	String chat(@UserMessage String message);
}