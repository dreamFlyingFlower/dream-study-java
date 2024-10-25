package dream.study.authorization.server.config;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import dream.flying.flower.ConstDigest;
import dream.flying.flower.digest.RsaHelper;
import dream.flying.flower.framework.security.constant.ConstAuthorization;
import dream.flying.flower.framework.security.entrypoint.LoginRedirectAuthenticationEntryPoint;
import dream.flying.flower.framework.security.handler.LoginFailureHandler;
import dream.flying.flower.framework.security.handler.LoginSuccessHandler;
import dream.study.authorization.server.context.RedisSecurityContextRepository;
import dream.study.authorization.server.helpers.SecurityContextOAuth2Helpers;
import dream.study.authorization.server.provider.sms.SmsGrantAuthenticationConverter;
import dream.study.authorization.server.provider.sms.SmsGrantAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * SpringSecurity5.8.14认证服务器配置
 * 
 * @author 飞花梦影
 * @date 2024-09-18 22:02:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class AuthorizationServerConfig {

	/**
	 * 登录地址,前后端分离就填写完整的url路径,不分离填写相对路径
	 */
	private final String LOGIN_URL = "http://127.0.0.1:5173";

	private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

	private final RedisSecurityContextRepository redisSecurityContextRepository;

	/**
	 * 配置认证相关的端点过滤器链,用于处理与协议端点相关的请求和响应.
	 * 
	 * 负责处理OAuth2和OpenID Connect的协议细节,例如授权请求、令牌颁发和验证等
	 *
	 * @param http security核心配置类
	 * @return 过滤器链
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			RegisteredClientRepository registeredClientRepository,
			AuthorizationServerSettings authorizationServerSettings) throws Exception {
		// 默认配置,忽略认证端点的csrf校验.如果要整合OAuth2,则需要当前方式注入相关对象
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

		// 新建设备码converter和provider
		// DeviceClientAuthenticationConverter deviceClientAuthenticationConverter =
		// new
		// DeviceClientAuthenticationConverter(authorizationServerSettings.getDeviceAuthorizationEndpoint());
		// DeviceClientAuthenticationProvider deviceClientAuthenticationProvider =
		// new DeviceClientAuthenticationProvider(registeredClientRepository);

		// 使用redis存储、读取登录的认证信息
		http.securityContext(context -> context.securityContextRepository(redisSecurityContextRepository));

		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				// 开启OpenID Connect 1.0协议相关端点
				.oidc(Customizer.withDefaults())
				// 设置自定义用户确认授权页
				.authorizationEndpoint(
						authorizationEndpoint -> authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
		// 设置设备码用户验证url(自定义用户验证页)
		// .deviceAuthorizationEndpoint(
		// deviceAuthorizationEndpoint ->
		// deviceAuthorizationEndpoint.verificationUri("/activate"))
		// 设置验证设备码用户确认页面
		// .deviceVerificationEndpoint(
		// deviceVerificationEndpoint ->
		// deviceVerificationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
		// .clientAuthentication(clientAuthentication ->
		// // 客户端认证添加设备码的converter和provider
		// clientAuthentication.authenticationConverter(deviceClientAuthenticationConverter)
		// .authenticationProvider(deviceClientAuthenticationProvider))
		;
		http
				// 当未登录时访问认证端点时重定向至login页面
				// .exceptionHandling((exceptions) -> exceptions
				// .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
				// 当未登录时访问认证端点时重定向至login页面,同时指定请求类型
				.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(
						new LoginRedirectAuthenticationEntryPoint(LOGIN_URL),
						new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

		// 接受访问令牌用于用户信息和/或客户端注册
		http.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

		// 自定义短信认证登录转换器
		SmsGrantAuthenticationConverter converter = new SmsGrantAuthenticationConverter();
		// 自定义短信认证登录认证提供
		SmsGrantAuthenticationProvider provider = new SmsGrantAuthenticationProvider();
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				// 让认证服务器元数据中有自定义的认证方式
				.authorizationServerMetadataEndpoint(metadata -> metadata.authorizationServerMetadataCustomizer(
						customizer -> customizer.grantType(ConstAuthorization.GRANT_TYPE_SMS_CODE)))
				// 添加自定义grant_type——短信认证登录
				.tokenEndpoint(tokenEndpoint -> tokenEndpoint.accessTokenRequestConverter(converter)
						.authenticationProvider(provider));

		DefaultSecurityFilterChain build = http.build();

		// 从框架中获取provider中所需的bean
		OAuth2TokenGenerator<?> tokenGenerator = http.getSharedObject(OAuth2TokenGenerator.class);
		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);
		// 以上三个bean在build()方法之后调用是因为调用build方法时框架会尝试获取这些类,
		// 如果获取不到则初始化一个实例放入SharedObject中,所以要在build方法调用之后获取
		// 在通过set方法设置进provider中,但是如果在build方法之后调用authenticationProvider(provider)
		// 框架会提示unsupported_grant_type,因为已经初始化完了,在添加就不会生效了
		provider.setTokenGenerator(tokenGenerator);
		provider.setAuthorizationService(authorizationService);
		provider.setAuthenticationManager(authenticationManager);

		return build;
	}

	/**
	 * 配置资源相关的过滤器链,不能和上面的{@link #authorizationServerSecurityFilterChain()}放一起,会有冲突
	 * 
	 * 用于身份验证的SpringSecurity过滤器链,用于处理身份验证相关的请求和响应.负责验证用户的身份,并生成相应的凭据,以便后续的授权和访问控制
	 *
	 * @param http security核心配置类
	 * @return 过滤器链
	 * @throws Exception 抛出
	 */
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		// 添加跨域过滤器
		http.addFilter(corsFilter());
		// 禁用 csrf 与 cors
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests((authorize) -> authorize
				// 放行静态资源
				.requestMatchers("/assets/**", "/webjars/**", "/login", "/getCaptcha", "/getSmsCaptcha")
				.permitAll()
				.anyRequest()
				.authenticated())
				// 指定登录页面
				.formLogin(formLogin -> {
					formLogin.loginPage("/login");
					if (UrlUtils.isAbsoluteUrl(LOGIN_URL)) {
						// 绝对路径代表是前后端分离,登录成功和失败改为写回json,不重定向了
						formLogin.successHandler(new LoginSuccessHandler());
						formLogin.failureHandler(new LoginFailureHandler());
					}
				});

		// 添加BearerTokenAuthenticationFilter,将认证服务当做一个资源服务,解析请求头中的token
		// 资源服务器配置,处理使用access_token访问用户信息端点和客户端注册端点
		http.oauth2ResourceServer((resourceServer) -> resourceServer
				// 可自定义JWT设置
				.jwt(Customizer.withDefaults())
				// 权限不足时的异常处理
				.accessDeniedHandler(SecurityContextOAuth2Helpers::exceptionHandler)
				// 未携带token的异常处理
				.authenticationEntryPoint(SecurityContextOAuth2Helpers::exceptionHandler));

		http
				// 当未登录时访问认证端点时重定向至login页面
				.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(
						new LoginRedirectAuthenticationEntryPoint(LOGIN_URL),
						new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

		// 使用redis存储、读取登录的认证信息
		http.securityContext(context -> context.securityContextRepository(redisSecurityContextRepository));

		return http.build();
	}

	/**
	 * 跨域过滤器配置
	 *
	 * @return CorsFilter
	 */
	@Bean
	CorsFilter corsFilter() {
		// 初始化cors配置对象
		CorsConfiguration configuration = new CorsConfiguration();
		// 设置跨域访问可以携带cookie
		configuration.setAllowCredentials(true);
		// 设置允许跨域的域名,如果允许携带cookie的话,路径就不能写*号, *表示所有的域名都可以跨域访问
		configuration.addAllowedOrigin("http://127.0.0.1:8080");
		// configuration.setAllowedOriginPatterns(Collections.singletonList(CorsConfiguration.ALL));
		// 允许所有的请求方法 ==> GET POST PUT Delete
		configuration.addAllowedMethod(CorsConfiguration.ALL);
		// 允许携带任何头信息
		configuration.addAllowedHeader(CorsConfiguration.ALL);
		// 初始化cors配置源对象
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		// 给配置源对象设置过滤的参数
		// 参数一: 过滤的路径 == > 所有的路径都要求校验是否跨域
		// 参数二: 配置类
		configurationSource.registerCorsConfiguration("/**", configuration);
		// 返回配置好的过滤器
		return new CorsFilter(configurationSource);
	}

	/**
	 * 自定义jwt,将权限信息放至jwt中
	 *
	 * @return OAuth2TokenCustomizer的实例
	 */
	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
		return context -> {
			// 检查登录用户信息是不是UserDetails,排除掉没有用户参与的流程
			if (context.getPrincipal().getPrincipal() instanceof UserDetails) {
				UserDetails user = (UserDetails) context.getPrincipal().getPrincipal();
				// 获取申请的scopes
				Set<String> scopes = context.getAuthorizedScopes();
				// 获取用户的权限
				Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
				// 提取权限并转为字符串
				Set<String> authoritySet = Optional.ofNullable(authorities)
						.orElse(Collections.emptyList())
						.stream()
						// 获取权限字符串
						.map(GrantedAuthority::getAuthority)
						// 去重
						.collect(Collectors.toSet());

				// 合并scope与用户信息
				authoritySet.addAll(scopes);

				JwtClaimsSet.Builder claims = context.getClaims();
				// 将权限信息放入jwt的claims中,也可以生成一个以指定字符分割的字符串放入
				claims.claim(ConstAuthorization.AUTHORITIES_KEY, authoritySet);
				// 放入其它自定内容
				// 角色、头像...
			}
		};
	}

	/**
	 * 自定义jwt解析器,设置解析出来的权限信息的前缀与在jwt中的key
	 *
	 * @return jwt解析器 JwtAuthenticationConverter
	 */
	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		// 设置解析权限信息的前缀,设置为空是去掉前缀
		grantedAuthoritiesConverter.setAuthorityPrefix("");
		// 设置权限信息在jwt claims中的key
		grantedAuthoritiesConverter.setAuthoritiesClaimName(ConstAuthorization.AUTHORITIES_KEY);

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	/**
	 * 将AuthenticationManager注入ioc中,其它需要使用地方可以直接从ioc中获取
	 *
	 * @param authenticationConfiguration 导出认证配置
	 * @return AuthenticationManager 认证管理器
	 */
	@Bean
	@SneakyThrows
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * 配置密码解析器,注意重复注入
	 *
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 内存中注入UserDetailsService,注意重复注入
	 * 
	 * UserDetailsService的实例,用于获取需要进行身份验证的用户信息,提供了与用户相关的数据,以便进行身份验证和授权的决策
	 * 
	 * @return UserDetailsService
	 */
	@Bean
	UserDetailsService userDetailsService() {
		UserDetails userDetails = User.builder()
				.passwordEncoder(passwordEncoder()::encode)
				.username("username")
				.password("123456")
				.roles("USRE")
				.build();

		return new InMemoryUserDetailsManager(userDetails);
	}

	/**
	 * 配置基于db的oauth2的授权管理服务
	 *
	 * @param jdbcTemplate db数据源信息
	 * @param registeredClientRepository 上边注入的客户端repository
	 * @return JdbcOAuth2AuthorizationService
	 */
	@Bean
	OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		// 基于db的oauth2认证服务,基于内存的服务实现InMemoryOAuth2AuthorizationService
		return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * 配置基于db的授权确认管理服务
	 *
	 * @param jdbcTemplate db数据源信息
	 * @param registeredClientRepository 客户端repository
	 * @return JdbcOAuth2AuthorizationConsentService
	 */
	@Bean
	OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		// 基于db的授权确认管理服务,基于内存的服务实现InMemoryOAuth2AuthorizationConsentService
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * 配置jwk源,使用非对称加密,公开用于检索匹配指定选择器的JWK的方法
	 * 
	 * 用于生成和管理用于对访问令牌进行签名的JSON Web Key(JWK),提供了加密算法和密钥,以确保访问令牌的完整性和安全性
	 *
	 * @return JWKSource
	 */
	@Bean
	JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = RsaHelper.generateKeyPair(ConstDigest.KEY_SIZE_2048);
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey =
				new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	/**
	 * 配置jwt解析器
	 * 
	 * 用于验证和解码已签名的访问令牌,以获取其中包含的授权信息和用户身份,负责验证访问令牌的有效性和真实性
	 *
	 * @param jwkSource jwk源
	 * @return JwtDecoder
	 */
	@Bean
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	/**
	 * 添加认证服务器配置,设置jwt签发者、默认端点请求地址等
	 * 
	 * 配置Authorization Server的一些全局设置,例如令牌的有效期、刷新令牌的策略和认证页面的URL等,提供了对授权服务器行为的细粒度控制
	 *
	 * @return AuthorizationServerSettings
	 */
	@Bean
	AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				/*
				 * 设置token签发地址(http(s)://{ip}:{port}/context-path,
				 * http(s)://domain.com/context-path)
				 * 如果需要通过ip访问这里就是ip,如果是有域名映射就填域名,通过什么方式访问该服务这里就填什么
				 */
				// .issuer("http://127.0.0.1:8080")
				.build();
	}
}