package com.wy.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ResourceUtils;

import com.wy.common.Constants;
import com.wy.properties.WeixinProperties;

/**
 * 退款认证
 * 
 * @author 飞花梦影
 * @date 2021-12-29 23:50:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ClientCustomSSL {

	public static String doRefund(String url, String data) throws Exception {
		WeixinProperties properties = SpringContextUtils.getBean(WeixinProperties.class);
		/**
		 * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
		 */
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		File certfile =
				ResourceUtils.getFile("classpath:cert" + Constants.SF_FILE_SEPARATOR + properties.getCertPath());
		FileInputStream instream = new FileInputStream(certfile);
		try {
			keyStore.load(instream, properties.getMchId().toCharArray());
		} finally {
			instream.close();
		}
		SSLContext sslcontext =
				SSLContexts.custom().loadKeyMaterial(keyStore, properties.getMchId().toCharArray()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				// SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
				new DefaultHostnameVerifier());
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpost = new HttpPost(url);
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();
				String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
				EntityUtils.consume(entity);
				return jsonStr;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}
}