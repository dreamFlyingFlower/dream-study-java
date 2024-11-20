package com.wy.signature;

import lombok.Data;

/**
 * 读取公钥和私钥,其他路径中的字符串优先级更高
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:29:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class KeyPairProperties {

	// private MessageDigestType messageDigestType;

	private String publicKeyPath;

	private String publicKey;

	private String privateKeyPath;

	private String privateKey;
}