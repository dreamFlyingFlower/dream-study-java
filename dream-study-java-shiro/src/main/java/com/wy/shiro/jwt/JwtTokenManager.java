package com.wy.shiro.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.wy.collection.MapTool;
import com.wy.shiro.properties.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 自定义jwtkoken管理者
 * 
 * @author 飞花梦影
 * @date 2022-06-22 13:36:47
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
@EnableConfigurationProperties({ JwtProperties.class })
public class JwtTokenManager {

	@Autowired
	private JwtProperties jwtProperties;

	/**
	 * 签发令牌 1、头部型 密码签名 加密算法 2、payload 签发的时间 唯一标识 签发者 过期时间
	 * 
	 * @param iss 签发者
	 * @param ttlMillis 过期时间
	 * @param claims jwt存储的一些非隐私信息
	 * @return
	 */
	public String issuedToken(String iss, long ttlMillis, String sessionId, Map<String, Object> claims) {
		if (MapTool.isEmpty(claims)) {
			claims = new HashMap<>();
		}
		// 获取当前时间
		long nowMillis = System.currentTimeMillis();
		// 构建令牌
		JwtBuilder builder = Jwts.builder()
		        // 构建非隐私信息
		        .setClaims(claims)
		        // 构建唯一标识,此时使用shiro生成的唯一id
		        .setId(sessionId)
		        // 构建签发时间
		        .setIssuedAt(new Date(nowMillis))
		        // 签发人,即JWT是给谁用的,一般是username,userId
		        .setSubject(iss)
		        // 指定算法和秘钥
		        .signWith(generateKey(), SignatureAlgorithm.HS256);
		if (ttlMillis > 0) {
			long expMillis = nowMillis + ttlMillis;
			Date expData = new Date(expMillis);
			// 指定过期时间
			builder.setExpiration(expData);
		}
		return builder.compact();
	}

	/**
	 * 解析令牌
	 * 
	 * @param jwtToken 令牌字符串
	 * @return
	 */
	public Claims decodeToken(String jwtToken) {
		// 带着密码去解析字符串
		return Jwts.parserBuilder().setSigningKey(generateKey()).build().parseClaimsJws(jwtToken).getBody();
	}

	/**
	 * 校验令牌:1、头部信息和荷载信息是否被篡改 2、校验令牌是否过期
	 * 
	 * @param jwtToken 令牌字符串
	 * @return
	 */
	public boolean isVerifyToken(String jwtToken) {
		// 带着签名构建校验对象
		Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getBase64EncodedSecretKey().getBytes());
		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		// 校验:1-头部信息和荷载信息是否被篡改;2-校验令牌是否过期;不通过则会抛出异常
		jwtVerifier.verify(jwtToken);
		return true;
	}

	private SecretKey generateKey() {
		String stringKey = jwtProperties.getBase64EncodedSecretKey();
		// 本地的密码解码
		byte[] encodedKey = Base64.getDecoder().decode(stringKey);
		// 根据给定的字节数组使用AES加密算法构造一个密钥
		return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
	}
}