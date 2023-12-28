package com.wy.signature;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import com.wy.digest.DigestHelper;
import com.wy.io.file.FileHelper;
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

	private void loadKeyPairByPath() {
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
		try {
			return FileHelper.read(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 验签
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

		// 公钥验签
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
	
	public static void main(String[] args) {
		try {
			List<String> lists = FileHelper.readLines(new File("F:\\test.txt"));
			String str = FileHelper.read("F:\\test.txt");
			System.out.println(String.join(",", lists));
			System.out.println(str);
			System.out.println(FileHelper.readOne(new File("F:\\test.txt") ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}