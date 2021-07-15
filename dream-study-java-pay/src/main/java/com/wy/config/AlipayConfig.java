package com.wy.config;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;

import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.wy.properties.ConfigPropertes;

/**
 * 支付宝配置
 * 
 * @author 飞花梦影
 * @date 2021-07-15 14:14:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
public class AlipayConfig {

	@Value("${custom.http.proxyHost}")
	private String proxyHost;

	@Value("${custom.http.proxyPort}")
	private int proxyPort;

	@Value("${spring.profiles.active}")
	private String activeEnv;

	@Autowired
	private ConfigPropertes config;

	@Bean(name = { "alipayClient" })
	public AlipayClient alipayClientService() throws Exception {
		CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
		// 设置网关地址
		certAlipayRequest.setServerUrl(config.getAlipay().getServerUrl());
		// 设置应用Id
		certAlipayRequest.setAppId(config.getAlipay().getAppId());
		// 设置应用私钥
		certAlipayRequest.setPrivateKey(config.getAlipay().getPrivateKey());
		// 设置请求格式，固定值json
		certAlipayRequest.setFormat(config.getAlipay().getFormat());
		// 设置字符集
		certAlipayRequest.setCharset(config.getAlipay().getCharset());
		// 设置签名类型
		certAlipayRequest.setSignType(config.getAlipay().getSignType());
		// 如果是生产环境或者预演环境，则使用代理模式
		if ("prod".equals(activeEnv) || "stage".equals(activeEnv) || "test".equals(activeEnv)) {
			// 设置应用公钥证书路径
			certAlipayRequest.setCertContent(getCertContentByPath(config.getAlipay().getAppCertPath()));
			// 设置支付宝公钥证书路径
			certAlipayRequest.setAlipayPublicCertContent(getCertContentByPath(config.getAlipay().getAlipayCertPath()));
			// 设置支付宝根证书路径
			certAlipayRequest.setRootCertContent(getCertContentByPath(config.getAlipay().getAlipayRootCertPath()));
			certAlipayRequest.setProxyHost(proxyHost);
			certAlipayRequest.setProxyPort(proxyPort);

		} else {
			// local
			String serverPath = this.getClass().getResource("/").getPath();
			// 设置应用公钥证书路径
			certAlipayRequest.setCertPath(serverPath + config.getAlipay().getAppCertPath());
			// 设置支付宝公钥证书路径
			certAlipayRequest.setAlipayPublicCertPath(serverPath + config.getAlipay().getAlipayCertPath());
			// 设置支付宝根证书路径
			certAlipayRequest.setRootCertPath(serverPath + config.getAlipay().getAlipayRootCertPath());
		}
		return new DefaultAlipayClient(certAlipayRequest);
	}

	public String getCertContentByPath(String name) {
		InputStream inputStream = null;
		String content = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream(name);
			content = new String(FileCopyUtils.copyToByteArray(inputStream));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
}