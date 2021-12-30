package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信支付参数
 *
 * @author 飞花梦影
 * @date 2021-12-28 17:01:40
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
@ConfigurationProperties(prefix = "config.weixin")
@Getter
@Setter
public class WeixinProperties {

	/** 服务号的应用ID */
	private String appId = "XXXXXXXXXXXXXXXX";

	/** 服务号的应用密钥 */
	private String appSecret = "XXXXXXXXXXXXXXXX";

	/** 服务号的配置token */
	private String token = "XXXXXXXXXXXXXXXX";

	/** 商户号 */
	private String mchId = "XXXXXXXXXXXXXXXX";

	/** API密钥 */
	private String apiKey = "XXXXXXXXXXXXXXXX";

	/** 签名加密方式 */
	private String signType = "MD5";

	/** 微信支付证书名称 */
	private String certPath = "apiclient_cert.p12";

	/** 微信后台回调 */
	private String notifyUrl = "";
}