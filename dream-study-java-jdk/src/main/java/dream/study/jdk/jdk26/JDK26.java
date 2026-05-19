package dream.study.jdk.jdk26;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.logging.Logger;

/**
 * JDK26新特性
 * 
 * <pre>
 * 1.在 instanceof 中使用原始类型
 * 2.在 switch 中匹配原始类型
 * 3.守卫模式与记录解构
 * 4.穷尽性检查: switch 中 boolean 不再需要 default
 * 5.性能优化: G1 GC 通过降低同步开销提升吞吐量
 * 6.启动加速: 任意 GC 下的 AOT 对象缓存
 * 7.安全加密: 加密对象(公私钥、证书、CRL)和 PEM 文本之间互转,过去要 Base64 + Header/Footer 拼接,要么引入 Bouncy Castle 第三方库.
 * 		JEP 524 终于把这件事内置进 JDK——通过 java.security.PEMEncoder 和 java.security.PEMDecoder 两个新类.
 * 		JEP 524 仍是预览特性(第 2 次预览),需要加 --enable-preview
 * 		PEMEncoder 还支持给私钥加密(PKCS#8 EncryptedPrivateKeyInfo)
 * 8.库改进: 让 final 真正意味 final.对 final 字段做反射修改会触发警告,并在未来版本里逐步变成错误
 * 9.网络新协议: HttpClient 支持 HTTP/3.HTTP/3 把传输层从 TCP 换成了基于 UDP 的 QUIC,带来了更快的握手、更低的延迟,以及对网络丢包的更好容忍.
 * 		JDK 11 引入的 HttpClient 在 26 这一版终于把 HTTP/3 补齐了,API 几乎没有变化,只需要把 Version 换成 HTTP_3.
 * 		HTTP/3 真正的价值在移动网络、跨地域调用、CDN 拉取这些场景.如果微服务大量使用 HTTPS 拉取上游资源,升级到 JDK 26 + HTTP/3 之后,
 * 		首次连接的握手时间会从两次 RTT 降到一次 RTT,整体响应时间能下降 10%~30%
 * 10.不可变数据: 延迟常量 LazyConstant.JDK 25 里引入的 StableValue 在 26 里重命名为 LazyConstant.
 * 		它解决了一个非常古老的问题,怎么写一个既线程安全、又像 final 字段那样能被 JIT 当成常量内联的"懒加载字段".
 * 		过去我们要么用 synchronized + double-check,要么用 Holder 内部类模式,但前者性能差、后者写起来啰嗦
 * 11.并发编程: 结构化并发的第六次预览.StructuredTaskScope 是 Java 在虚拟线程(Virtual Threads)落地之后,配套推出的高阶并发抽象.
 * 		它的核心理念是:把一组并发任务当成一个整体来管理,避免线程泄漏、避免取消时机错乱、避免错误处理散落各处.
 * 		相比 25 的版本做了几处细节打磨:
 * 			open() 现在接收 UnaryOperator<Builder> 而不是 Function
 * 			Joiner.allSuccessfulOrThrow() 直接返回 List<T> 而不是 Stream<T>
 * 			Joiner.anySuccessfulResultOrThrow() 改名为 anySuccessfulOrThrow()
 * 			新增了 onTimeout() 方法,超时时可以返回兜底结果而不是直接抛异常
 * 高性能计算: 向量 API 第 11 次孵化
 * 清理维护: JEP 504 正式移除 Applet API
 * </pre>
 *
 * @author 飞花梦影
 * @date 2026-05-19 14:17:07
 */
public class JDK26 {

	private static void feature1() {
		Object value = 42;

		if (value instanceof int i) {
			System.out.println("这是一个 int 值，等于 " + i);
		}

		Object decimal = 3.14;
		if (decimal instanceof double d) {
			System.out.println("这是一个 double 值，等于 " + d);
		}

		long big = 9_999_999_999L;
		if (big instanceof int small) {
			System.out.println("可以安全窄化为 int：" + small);
		} else {
			System.out.println("数值超出 int 范围，无法转换：" + big);
		}
	}

	private static void feature2() {
		Object obj = 42;

		switch (obj) {
		case int i -> System.out.println("整数：" + i);
		case double d -> System.out.println("小数：" + d);
		default -> System.out.println("其他类型");
		}
	}

	private static String feature3(Object temp) {
		return switch (temp) {
		case int t when t < 0 -> "严寒（" + t + "℃）";
		case int t when t < 10 -> "寒冷（" + t + "℃）";
		case int t when t < 20 -> "凉爽（" + t + "℃）";
		case int t when t < 30 -> "温暖（" + t + "℃）";
		case int t -> "炎热（" + t + "℃）";
		case double d when d < 0.0 -> "严寒（" + d + "℃）";
		case double d -> "高于零度（" + d + "℃）";
		default -> "无法识别的温度";
		};
	}

	private static String feature4(boolean flag) {
		return switch (flag) {
		case true -> "条件成立";
		case false -> "条件不成立";
		};
	}

	private static void feature7() {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair keyPair = generator.generateKeyPair();

		PEMEncoder encoder = PEMEncoder.of();
		String publicPem = encoder.encodeToString(keyPair.getPublic());
		String privatePem = encoder.encodeToString(keyPair.getPrivate());

		Files.writeString(Path.of("public.pem"), publicPem);
		Files.writeString(Path.of("private.pem"), privatePem);
		System.out.println("生成密钥完成，写入 public.pem / private.pem");

		PEMDecoder decoder = PEMDecoder.of();
		PublicKey loadedPublic = decoder.decode(Files.readString(Path.of("public.pem")), PublicKey.class);
		PrivateKey loadedPrivate = decoder.decode(Files.readString(Path.of("private.pem")), PrivateKey.class);

		System.out.println("公钥算法：" + loadedPublic.getAlgorithm());
		System.out.println("私钥算法：" + loadedPrivate.getAlgorithm());
		System.out.println("私钥与原始私钥一致：" + loadedPrivate.equals(keyPair.getPrivate()));

		// 私钥加密
		char[] password = "MyStrongPassword".toCharArray();
		PEMEncoder secureEncoder = PEMEncoder.of().withEncryption(password);
		String encryptedPrivatePem = secureEncoder.encodeToString(keyPair.getPrivate());

		// 私钥解密
		PEMDecoder secureDecoder = PEMDecoder.of().withDecryption(password);
		PrivateKey privateKey = secureDecoder.decode(encryptedPrivatePem, PrivateKey.class);
	}

	public static void feature9() throws Exception {
		HttpClient client = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_3)
				.connectTimeout(Duration.ofSeconds(5))
				.build();

		HttpRequest request = HttpRequest.newBuilder(URI.create("https://www.cloudflare.com/")).GET().build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println("协议版本：" + response.version());
		System.out.println("状态码：" + response.statusCode());
		System.out.println("Body 前 120 字符：" + response.body().substring(0, 120));

		// 如果服务器同时支持 HTTP/2 和 HTTP/3,可以让客户端自动协商，优先用 HTTP/3,失败时回退
		HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_3).build();

		HttpRequest request = HttpRequest.newBuilder(URI.create("https://www.cloudflare.com/"))
				.setOption(HttpOption.H3_DISCOVERY, Http3DiscoveryMode.ALT_SVC)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println("最终协议：" + response.version());

		// 异步流式发送也支持 HTTP/3，写法和过去完全一致：
		HttpRequest streamReq = HttpRequest.newBuilder(URI.create("https://example.com/stream"))
				.version(HttpClient.Version.HTTP_3)
				.GET()
				.build();

		client.sendAsync(streamReq, HttpResponse.BodyHandlers.ofLines())
				.thenAccept(resp -> resp.body().forEach(System.out::println))
				.join();
	}

	private static final LazyConstant<Logger> LOGGER = LazyConstant.of(() -> Logger.getLogger("LazyConstantDemo"));

	private static final LazyConstant<String> EXPENSIVE_CONFIG = LazyConstant.of(LazyConstantDemo::loadConfig);

	private static String loadConfig() {
		System.out.println("[loadConfig] 这是一次重量级的初始化");
		try {
			Thread.sleep(500);
		} catch (InterruptedException ignore) {
		}
		return "url=jdbc:mysql://localhost:3306/db_demo?user=root&password=123456";
	}

	public static void feature10() {
		LOGGER.get().info("应用启动");
		System.out.println("第一次读取配置：" + EXPENSIVE_CONFIG.get());
		System.out.println("第二次读取配置：" + EXPENSIVE_CONFIG.get());
		LOGGER.get().info("应用退出");
	}

	record User(long id, String name) {
	}

	record Order(long id, double amount) {
	}

	record Dashboard(User user, Order latestOrder) {
	}

	public static void feature11(String[] args) throws Exception {
		// 整段代码里没有 ExecutorService,没有 Future.get(),没有 try-catch
		// 任务的生命周期、异常传播、线程取消全部由 scope 接管
		// 如果 fetchUser 抛了异常,fetchLatestOrder 会被自动取消;反之亦然
		try (var scope = StructuredTaskScope.open()) {
			Subtask<User> userTask = scope.fork(() -> fetchUser(1001L));
			Subtask<Order> orderTask = scope.fork(() -> fetchLatestOrder(1001L));

			scope.join();

			Dashboard dashboard = new Dashboard(userTask.get(), orderTask.get());
			System.out.println("聚合结果：" + dashboard);
		}
	}

	static User fetchUser(long id) throws InterruptedException {
		Thread.sleep(300);
		return new User(id, "张三");
	}

	static Order fetchLatestOrder(long id) throws InterruptedException {
		Thread.sleep(200);
		return new Order(20260518L, 199.00);
	}

	public static String feature11_1() throws Exception {
		// 如果只想"任意一个成功就好"（多机房备份查询的典型场景）,可以用 anySuccessfulOrThrow 这个内置 Joiner
		try (var scope = StructuredTaskScope.open(Joiner.<String>anySuccessfulOrThrow())) {
			scope.fork(() -> fetchFrom("us-east"));
			scope.fork(() -> fetchFrom("eu-west"));
			scope.fork(() -> fetchFrom("ap-southeast"));
			return scope.join();
		}
	}

	private static String fetchFrom(String region) throws InterruptedException {
		long latency = (long) (Math.random() * 500);
		Thread.sleep(latency);
		return region + " 返回结果 (latency=" + latency + "ms)";
	}

	public static String feature11_2() throws Exception {
		// 要求"必须全部成功,否则取消"的场景则用 allSuccessfulOrThrow
		try (var scope = StructuredTaskScope.open(Joiner.allSuccessfulOrThrow())) {
			var profile = scope.fork(() -> fetchProfile(1L));
			var prefs = scope.fork(() -> fetchPreferences(1L));
			var perms = scope.fork(() -> fetchPermissions(1L));

			java.util.List<Object> results = scope.join();
			System.out.println("全部完成：" + results);
		}

		// 加超时
		try (var scope = StructuredTaskScope.open(Joiner.<String>anySuccessfulOrThrow(),
				builder -> builder.withTimeout(Duration.ofMillis(300)))) {

			scope.fork(() -> slowFetch("A"));
			scope.fork(() -> slowFetch("B"));
			System.out.println("结果：" + scope.join());
		}
	}
}