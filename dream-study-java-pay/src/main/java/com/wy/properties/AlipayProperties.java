package com.wy.properties;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付宝参数
 *
 * @author 飞花梦影
 * @date 2021-07-15 14:05:31
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
@ConfigurationProperties(prefix = "config.alipay")
@Getter
@Setter
public class AlipayProperties {
	
	private String pid;

	/** 当面付的APPID */
	private String appId = "appId";

	/** 支付宝网关名 */
	private String serverUrl = "https://openapi.alipay.com/gateway.do";

	private String format = "json";

	private String charset = StandardCharsets.UTF_8.name();

	/** 签名类型 */
	private String signType = "RSA2";

	private String privateKey = "privateKey";
	
	private String publicKey = "publicKey";

	private String appCertPath = "/cert/appCertPublicKey_202105164652941.crt";

	private String alipayCertPath = "/cert/alipayCertPublicKey_RSA2.crt";

	private String alipayRootCertPath = "/cert/alipayRootCert.crt";

	/** 支付宝回调地址 */
	private String notifyUrl = "";

	/** 当面付最大查询次数和查询间隔,单位毫秒 */
	private Integer maxQueryRetry = 5;

	private Integer queryDuration = 5000;

	/** 当面付最大撤销次数和撤销间隔,单位毫秒 */
	private Integer maxCancelRetry = 3;

	private Integer cancelDuration = 2000;

	/** 交易保障线程第一次调度延迟和调度间隔,单位秒 */
	private Integer heartbeatDelay = 5;

	private Integer heartbeatDuration = 900;
}