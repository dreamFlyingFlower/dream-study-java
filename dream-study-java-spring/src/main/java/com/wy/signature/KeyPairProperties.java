package com.wy.signature;

import com.wy.digest.enums.MessageDigestType;

import lombok.Data;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:29:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
public class KeyPairProperties {

	private MessageDigestType messageDigestType;

	private String publicKeyPath;

	private String publicKey;

	private String privateKeyPath;

	private String privateKey;
}