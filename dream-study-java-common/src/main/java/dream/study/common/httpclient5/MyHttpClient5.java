package dream.study.common.httpclient5;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HeaderElements;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.message.MessageSupport;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.Args;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * HttpClient5
 * 
 * @author 飞花梦影
 * @date 2024-04-25 16:51:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyHttpClient5 {

	/** 可自定义keep-alive,参考 DefaultConnectionKeepAliveStrategy */
	private static ConnectionKeepAliveStrategy defaultConnectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {

		@Override
		public TimeValue getKeepAliveDuration(HttpResponse response, HttpContext context) {
			Args.notNull(response, "HTTP response");
			final Iterator<HeaderElement> it = MessageSupport.iterate(response, HeaderElements.KEEP_ALIVE);
			while (it.hasNext()) {
				final HeaderElement he = it.next();
				final String param = he.getName();
				final String value = he.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) {
					try {
						return TimeValue.ofSeconds(Long.parseLong(value));
					} catch (final NumberFormatException ignore) {
					}
				}
			}

			// final HttpClientContext clientContext = HttpClientContext.adapt(context);
			// final RequestConfig requestConfig = clientContext.getRequestConfig();
			// return requestConfig.getConnectionKeepAlive();
			return TimeValue.ofSeconds(60L);
		}
	};

	private static PoolingHttpClientConnectionManager defaultHttpClientConnectionManager =
			new PoolingHttpClientConnectionManager();

	static {
		// 设置最大线程数
		defaultHttpClientConnectionManager.setMaxTotal(500);
		// 默认每路由最高50并发
		defaultHttpClientConnectionManager.setDefaultMaxPerRoute(50);
	}

	/** Basic认证 */
	private static final CredentialsProvider credsProvider;

	/** httpClient */
	private static final CloseableHttpClient httpClient;

	/** httpGet方法 */
	private static final HttpGet httpGet;

	/** 请求配置 */
	private static final RequestConfig requestConfig;

	/** 响应处理器 */
	private static final HttpClientResponseHandler<String> httpClientResponseHandler;

	public static CloseableHttpClient httpClient() {
		return HttpClients.custom()
				.setConnectionManager(defaultHttpClientConnectionManager)
				.setKeepAliveStrategy(defaultConnectionKeepAliveStrategy)
				// 设置http client的重试次数,默认是1次,间隔1S
				.setRetryStrategy(new DefaultHttpRequestRetryStrategy())
				.setDefaultRequestConfig(RequestConfig.custom().build())
				.build();
	}

	// jackson解析工具
	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		System.setProperty("http.maxConnections", "50");
		System.setProperty("http.keepAlive", "true");
		// 设置basic校验
		BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
		basicCredentialsProvider.setCredentials(new AuthScope("localhost", 2222),
				new UsernamePasswordCredentials("", "".toCharArray()));
		credsProvider = basicCredentialsProvider;
		// 创建http客户端
		httpClient = HttpClients.custom()
				.useSystemProperties()
				.setRetryStrategy(new DefaultHttpRequestRetryStrategy())
				.setDefaultCredentialsProvider(credsProvider)
				.build();
		// 初始化httpGet
		httpGet = new HttpGet("");
		// 初始化HTTP请求配置
		requestConfig = RequestConfig.custom()
				.setContentCompressionEnabled(true)
				.setAuthenticationEnabled(true)
				.setConnectionRequestTimeout(Timeout.ofSeconds(100))
				.setResponseTimeout(Timeout.ofSeconds(100))
				.build();
		httpGet.setConfig(requestConfig);
		// 初始化response解析器
		httpClientResponseHandler = new BasicHttpClientResponseHandler();
	}

	/**
	 * 返回响应
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getResponse(String url) throws IOException {
		HttpGet get = new HttpGet(url);
		String response = httpClient.execute(get, httpClientResponseHandler);
		return response;
	}

	/**
	 * 发送http请求,并用jackson工具解析
	 * 
	 * @param url
	 * @return
	 */
	public static JsonNode getUrl1(String url) {
		try {
			httpGet.setUri(URI.create(url));
			String response = httpClient.execute(httpGet, httpClientResponseHandler);
			JsonNode node = mapper.readTree(response);
			return node;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送http请求,并用fastjson工具解析
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject getUrl3(String url) {
		try {
			httpGet.setUri(URI.create(url));
			String response = httpClient.execute(httpGet, httpClientResponseHandler);
			JSONObject jsonObject = JSONObject.parseObject(response);
			return jsonObject;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}