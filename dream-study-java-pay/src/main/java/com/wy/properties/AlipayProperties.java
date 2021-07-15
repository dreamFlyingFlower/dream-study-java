package com.wy.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付宝参数
 *
 * @author 飞花梦影
 * @date 2021-07-15 14:05:31
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
public class AlipayProperties {

	private String appId = "appId";

	private String serverUrl = "https://openapi.alipay.com/gateway.do";

	private String privateKey = "privateKey";

	private String format = "json";

	private String charset = "UTF-8";

	private String signType = "RSA2";

	private String appCertPath = "/cert/appCertPublicKey_202105164652941.crt";

	private String alipayCertPath = "/cert/alipayCertPublicKey_RSA2.crt";

	private String alipayRootCertPath = "/cert/alipayRootCert.crt";
}