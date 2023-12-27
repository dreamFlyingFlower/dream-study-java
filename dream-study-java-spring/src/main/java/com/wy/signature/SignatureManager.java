package com.wy.signature;

import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.wy.digest.DigestHelper;
import com.wy.io.IOHelper;
import com.wy.lang.StrHelper;

import lombok.Getter;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:31:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ConditionalOnBean(SignatureProperties.class)
@Getter
@Component
public class SignatureManager {

	private final SignatureProperties signatureProperties;

	public SignatureManager(SignatureProperties signatureProperties) {
		this.signatureProperties = signatureProperties;
		loadKeyPairByPath();
	}

	/**
	 * 验签。验证不通过可能抛出运行时异常CryptoException
	 *
	 * @param appId 调用方的唯一标识
	 * @param rawData 原数据
	 * @param signature 待验证的签名(十六进制字符串)
	 * @return 验证是否通过
	 */
	public boolean verifySignature(String appId, String rawData, String signature) {
		KeyPairProperties keyPairProperties = getKeyPairProperties(appId);
		if (Objects.isNull(keyPairProperties)) {
			return false;
		}

		// 使用公钥验签
		return DigestHelper.RSAVerify(rawData, signature, keyPairProperties.getPublicKey());
	}

	/**
	 * 生成签名
	 *
	 * @param appId 调用方的唯一标识
	 * @param rawData 原数据
	 * @return 签名(十六进制字符串)
	 */
	public String sign(String appId, String rawData) {
		KeyPairProperties keyPairProperties = getKeyPairProperties(appId);
		if (Objects.isNull(keyPairProperties)) {
			return null;
		}
		if (StrHelper.isBlank(keyPairProperties.getPrivateKey())) {
			return null;
		}
		return DigestHelper.RSASignString(rawData.getBytes(), keyPairProperties.getPrivateKey());
	}

	public KeyPairProperties getKeyPairProperties(String appId) {
		return signatureProperties.getKeyPair().get(appId);
	}

	/**
	 * 加载非对称密钥对
	 */
	private void loadKeyPairByPath() {
		// 支持类路径配置,形如:classpath:secure/public.txt
		// 公钥和私钥都是base64编码后的字符串
		signatureProperties.getKeyPair().forEach((key, keyPairProps) -> {
			keyPairProps.setPublicKey(Optional.ofNullable(loadKeyByPath(keyPairProps.getPublicKeyPath()))
					.orElse(keyPairProps.getPublicKey()));
			keyPairProps.setPrivateKey(Optional.ofNullable(loadKeyByPath(keyPairProps.getPrivateKeyPath()))
					.orElse(keyPairProps.getPrivateKey()));
			if (StrHelper.isBlank(keyPairProps.getPublicKey()) || StrHelper.isBlank(keyPairProps.getPrivateKey())) {
				throw new RuntimeException("No public and private key files configured");
			}
		});
	}

	private String loadKeyByPath(String path) {
		if (StrHelper.isBlank(path)) {
			return null;
		}
		return IOHelper.readUtf8(ResourceUtils.getFile(path));
	}
}