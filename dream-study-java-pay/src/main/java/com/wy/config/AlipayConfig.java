package com.wy.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.FileCopyUtils;

import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.wy.properties.AlipayProperties;
import com.wy.properties.HttpProperties;
import com.wy.util.ArrayTool;

/**
 * 支付宝配置
 * 
 * @author 飞花梦影
 * @date 2021-07-15 14:14:34
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
public class AlipayConfig {

	@Autowired
	private Environment environment;

	@Bean(name = "alipayClient")
	public AlipayClient alipayClient(AlipayProperties alipay, HttpProperties http) throws Exception {
		CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
		// 设置网关地址
		certAlipayRequest.setServerUrl(alipay.getServerUrl());
		// 设置应用Id
		certAlipayRequest.setAppId(alipay.getAppId());
		// 设置应用私钥
		certAlipayRequest.setPrivateKey(alipay.getPrivateKey());
		// 设置请求格式，固定值json
		certAlipayRequest.setFormat(alipay.getFormat());
		// 设置字符集
		certAlipayRequest.setCharset(alipay.getCharset());
		// 设置签名类型
		certAlipayRequest.setSignType(alipay.getSignType());
		// 如果是生产环境或者预演环境,则使用代理模式
		String[] profiles = environment.getActiveProfiles();
		if (ArrayTool.contains(profiles, "prod") || ArrayTool.contains(profiles, "test")) {
			// 设置应用公钥证书内容
			certAlipayRequest.setCertContent(getCertContentByPath(alipay.getAppCertPath()));
			// 设置支付宝公钥证书内容
			certAlipayRequest.setAlipayPublicCertContent(getCertContentByPath(alipay.getAlipayCertPath()));
			// 设置支付宝根证书内容
			certAlipayRequest.setRootCertContent(getCertContentByPath(alipay.getAlipayRootCertPath()));
			certAlipayRequest.setProxyHost(http.getProxyHost());
			certAlipayRequest.setProxyPort(http.getProxyPort());
		} else {
			certAlipayRequest.setCertContent(getCertContentByPath(alipay.getAppCertPath()));
			certAlipayRequest.setAlipayPublicCertContent(getCertContentByPath(alipay.getAlipayCertPath()));
			certAlipayRequest.setRootCertContent(getCertContentByPath(alipay.getAlipayRootCertPath()));
		}
		return new DefaultAlipayClient(certAlipayRequest);
	}

	public String getCertContentByPath(String name) {
		String content = null;
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(name);) {
			content = new String(FileCopyUtils.copyToByteArray(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
}