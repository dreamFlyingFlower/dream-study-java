package dream.study.authorization.server.config;

import java.time.Duration;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import dream.flying.flower.framework.security.constant.ConstAuthorization;
import dream.study.authorization.server.core.CustomizerAuthorizationGrantType;
import lombok.RequiredArgsConstructor;

/**
 * SpringSecurity5.8.14客户端认证配置
 * 
 * {@link RegisteredClientRepository}:认证的客户端数据操作,自定义操作需实现该接口
 * {@link JdbcRegisteredClientRepository}:数据库认证客户端实现
 * {@link InMemoryRegisteredClientRepository}:内存认证客户端实现,可直接从配置文件中读取
 * 
 * @author 飞花梦影
 * @date 2024-09-18 22:02:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class AuthorizationClientConfig {

	/**
	 * 配置已经注册的客户端认证Repository,也可以直接使用配置文件
	 * 
	 * 用于管理OAuth2和OpenID Connect客户端的注册信息,包括客户端ID、密钥、授权类型和重定向URI等,负责验证客户端的身份并维护客户端的配置
	 *
	 * @param jdbcTemplate db 数据源信息
	 * @param passwordEncoder 密码解析器
	 * @return 基于数据库的repository
	 */
	@Bean
	RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
				// 客户端id
				.clientId("test-client")
				// 客户端秘钥,使用密码解析器加密
				.clientSecret(passwordEncoder.encode("123456"))
				// 客户端认证方式,基于请求头的认证
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				// 配置资源服务器使用该客户端获取授权时支持的方式
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				// 客户端添加自定义认证
				.authorizationGrantType(new AuthorizationGrantType(ConstAuthorization.GRANT_TYPE_SMS_CODE))
				// 授权码模式回调地址,oauth2.1已改为精准匹配,不能只设置域名,并且屏蔽了localhost,本机使用127.0.0.1访问
				.redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
				.redirectUri("https://www.baidu.com")
				// 该客户端的授权范围,OPENID与PROFILE是IdToken的scope,获取授权时请求OPENID的scope时认证服务会返回IdToken
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				// 自定scope
				.scope("message.read")
				.scope("message.write")
				// 客户端设置,设置用户需要确认授权
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				// token配置
				.tokenSettings(TokenSettings.builder()
						// access token 有效期
						.accessTokenTimeToLive(Duration.ofMinutes(60))
						.build())
				.build();

		// 基于内存的InMemoryRegisteredClientRepository
		// new InMemoryRegisteredClientRepository(registeredClient);

		// 基于db存储客户端
		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

		// 初始化客户端:如果没有该客户端信息,则存入客户端
		RegisteredClient repositoryByClientId =
				registeredClientRepository.findByClientId(registeredClient.getClientId());
		if (repositoryByClientId == null) {
			registeredClientRepository.save(registeredClient);
		}

		// 设备码授权客户端,公共客户端
		RegisteredClient deviceClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("device-message-client")
				// 公共客户端
				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
				// 设备码授权
				.authorizationGrantType(CustomizerAuthorizationGrantType.DEVICE_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				// 自定scope
				.scope("message.read")
				.scope("message.write")
				.build();
		RegisteredClient byClientId = registeredClientRepository.findByClientId(deviceClient.getClientId());
		if (byClientId == null) {
			registeredClientRepository.save(deviceClient);
		}

		// PKCE客户端
		RegisteredClient pkceClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("pkce-message-client")
				// 公共客户端
				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
				// 授权码模式,因为是扩展授权码流程,所以流程还是授权码的流程,改变的只是参数
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				// 授权码模式回调地址,oauth2.1已改为精准匹配,不能只设置域名,并且屏蔽了localhost,本机使用127.0.0.1访问
				.redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
				.clientSettings(ClientSettings.builder().requireProofKey(Boolean.TRUE).build())
				// 自定scope
				.scope("message.read")
				.scope("message.write")
				.build();
		RegisteredClient findPkceClient = registeredClientRepository.findByClientId(pkceClient.getClientId());
		if (findPkceClient == null) {
			registeredClientRepository.save(pkceClient);
		}
		return registeredClientRepository;
	}
}