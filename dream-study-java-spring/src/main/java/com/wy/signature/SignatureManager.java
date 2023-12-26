package com.wy.signature;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import com.wy.binary.HexHelper;
import com.wy.lang.StrHelper;
import com.wy.util.ResourceUtil;

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
	 * @param callerID 调用方的唯一标识
	 * @param rawData 原数据
	 * @param signature 待验证的签名(十六进制字符串)
	 * @return 验证是否通过
	 */
	public boolean verifySignature(String callerID, String rawData, String signature) {
		Sign sign = getSignByCallerID(callerID);
		if (Objects.isNull(sign)) {
			return false;
		}

		// 使用公钥验签
		return sign.verify(rawData.getBytes(StandardCharsets.UTF_8), HexHelper.decode(signature));
	}

	/**
	 * 生成签名
	 *
	 * @param callerID 调用方的唯一标识
	 * @param rawData 原数据
	 * @return 签名(十六进制字符串)
	 */
	public String sign(String callerID, String rawData) {
		Sign sign = getSignByCallerID(callerID);
		if (Objects.isNull(sign)) {
			return null;
		}
		return sign.signHex(rawData);
	}

	public KeyPairProperties getKeyPairPropsByCallerID(String callerID) {
		return signatureProperties.getKeyPair().get(callerID);
	}

	private Sign getSignByCallerID(String callerID) {
		KeyPairProperties keyPairProps = signatureProperties.getKeyPair().get(callerID);
		if (Objects.isNull(keyPairProps)) {
			return null;
		}
		return SecureUtil.sign(keyPairProps.getMessageDigestType(), keyPairProps.getPrivateKey(),
				keyPairProps.getPublicKey());
	}

	/**
	 * 加载非对称密钥对
	 */
	private void loadKeyPairByPath() {
		// 支持类路径配置,形如:classpath:secure/public.txt
		// 公钥和私钥都是base64编码后的字符串
		signatureProperties.getKeyPair().forEach((key, keyPairProps) -> {
			// 如果配置了XxxKeyPath，则优先XxxKeyPath
			keyPairProps.setPublicKey(loadKeyByPath(keyPairProps.getPublicKeyPath()));
			keyPairProps.setPrivateKey(loadKeyByPath(keyPairProps.getPrivateKeyPath()));
			if (StrHelper.isBlank(keyPairProps.getPublicKey()) || StrHelper.isBlank(keyPairProps.getPrivateKey())) {
				throw new RuntimeException("No public and private key files configured");
			}
		});
	}

	private String loadKeyByPath(String path) {
		if (StrHelper.isBlank(path)) {
			return null;
		}
		return IoUtil.readUtf8(ResourceUtil.getStream(path));
	}
}