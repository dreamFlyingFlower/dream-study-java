package com.wy.signature;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 签名相关配置
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:26:25
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConditionalOnProperty(value = "secure.signature.enable", havingValue = "true") // 根据条件注入bean
@Configuration
@ConfigurationProperties("config.security.signature")
public class SignatureProperties {

	private Boolean enable;

	private Map<String, KeyPairProperties> keyPair;
}