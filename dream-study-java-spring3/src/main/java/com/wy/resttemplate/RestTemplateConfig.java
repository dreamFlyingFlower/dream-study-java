package com.wy.resttemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorNetty2ClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.wy.actuator.MyConnectionKeepAliveStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link RestTemplate}:http调用,需要手动注入.{@link RestTemplateAutoConfiguration}只注入了RestTemplateBuilder
 * 
 * <pre>
 * {@link UriComponentsBuilder}:构造URI
 * {@link ServletUriComponentsBuilder}:构造相对于当前请求的URI
 * {@link MvcUriComponentsBuilder}:构造指向Controller的URI
 * {@link DefaultConnectionKeepAliveStrategy}
 * {@link ClientHttpRequestFactory}:RestTemplate默认的HTTP通用接口
 * ->{@link SimpleClientHttpRequestFactory}:默认的HTTP通用接口实现
 * ->{@link Netty4ClientHttpRequestFactory}:Netty的HTTP通用接口实现,5.0已废弃
 * ->{@link ReactorClientHttpConnector}:替代Netty4ClientHttpRequestFactory
 * ->{@link ReactorNetty2ClientHttpConnector}:替代Netty4ClientHttpRequestFactory
 * ->{@link HttpComponentsClientHttpRequestFactory}:Apache HttpClient的HTTP通用接口实现,需要引入httpclient
 * {@link PoolingHttpClientConnectionManager}:Apache的连接池管理,需要引入httpclient
 * {@link ConnectionKeepAliveStrategy}:连接超时管理,需要引入httpclient
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-10-02 09:41:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@Slf4j
public class RestTemplateConfig {

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	/** Https证书密钥资源地址 */
	@Value("${config.key-store}")
	private Resource keyStore;

	@Bean
	RestTemplate restTemplate() {
		restTemplateBuilder
				// 设置自定义的requestfactory
				.requestFactory(HttpComponentsClientHttpRequestFactory.class)
				// 设置连接超时和数据读取超时时间
				.connectTimeout(Duration.ofMillis(100))
				.readTimeout(Duration.ofMillis(500))
				.build();
		return restTemplateBuilder.build();
	}

	@Bean
	RestTemplate clientRestTemplate() {
		ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.defaults()
				.withConnectTimeout(Duration.ofSeconds(1))
				.withReadTimeout(Duration.ofSeconds(1));
		RestTemplate restTemplate = new RestTemplate(ClientHttpRequestFactoryBuilder.detect().build(settings));
		return restTemplate;
	}

	/**
	 * 利用httpclient构建自定义的requestfactory
	 */
	@SuppressWarnings("resource")
	@Bean
	HttpComponentsClientHttpRequestFactory requestFactory() {
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
				new PoolingHttpClientConnectionManager();
		poolingHttpClientConnectionManager
				.setDefaultConnectionConfig(ConnectionConfig.custom().setTimeToLive(30, TimeUnit.SECONDS).build());
		// 设置最大连接数
		poolingHttpClientConnectionManager.setMaxTotal(200);
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(20);

		poolingHttpClientConnectionManager.setDefaultTlsConfig(null);
		// TODO 如何添加ssl
		SSLContext sslContext = null;
		try {
			sslContext = SSLContextBuilder.create()
					// 会校验证书
					.loadTrustMaterial(keyStore.getURL(), "keyPass".toCharArray())
					// 放过所有证书校验
					// .loadTrustMaterial(null, (certificate, authType) -> true)
					.build();
		} catch (Exception e) {
			log.error("Exception occurred while creating SSLContext.", e);
		}
		System.out.println(sslContext);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(poolingHttpClientConnectionManager)
				// 关闭自动重试
				// .disableAutomaticRetries()
				.evictIdleConnections(TimeValue.of(30, TimeUnit.SECONDS))
				// 设置自定义的超时时间,默认是永久有效
				.setKeepAliveStrategy(new MyConnectionKeepAliveStrategy())
				// 设置Http认证证书
				// .setSSLContext(sslContext)
				// .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.build();

		return new HttpComponentsClientHttpRequestFactory(httpclient);
	}
}