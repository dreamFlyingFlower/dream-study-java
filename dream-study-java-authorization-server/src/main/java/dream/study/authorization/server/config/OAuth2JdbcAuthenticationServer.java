//package com.dream.oauth.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.CompositeTokenGranter;
//import org.springframework.security.oauth2.provider.TokenGranter;
//import org.springframework.security.oauth2.provider.TokenRequest;
//import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
//import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
//import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
//import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
//import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
//import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
//import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
//import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
//import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
///**
// * OAuth2使用数据库进行认证服务
// * 
// * 使用数据库保存服务Token,需要先建立表,详见 {@link JdbcClientDetailsService#BASE_FIND_STATEMENT}
// * 使用数据库保存服务Code,需要先建立表,详见
// * {@link JdbcAuthorizationCodeServices#DEFAULT_INSERT_STATEMENT}
// * 
// * {@link EnableAuthorizationServer}:添加该注解即可启动认证服务器,实现4种认证模式:授权码模式;密码模式;简化模式;客户端模式
// * 
// * 启动项目之后,可使用get请求默认访问/oauth/authorize?response_type=&client_id=&redirect_url=&scope,
// * 其中response_type为4中认证模式中的标识符,可从官网查看;client_id为分配给第三方服务的标识;
// * redirect_url为认证服务器返回给第三方服务器的授权码地址,授权码会拼接在该地址上;scope为权限,由服务提供商定义.
// * 访问该地址时会填一个用户名和密码,该用户名和密码就是相当于用户登录,同时需要给用户一个ROLE_USER的权限,否则登录失败
// * 
// * 获得授权码之后,再用post请求访问/oauth/token获得令牌信息,参数为grant_type,client_id,code,redirect_url,scope,见OAuth2官网
// * 
// * SpringOAuth获取令牌请求(/oauth/token)的核心流程:
// * 
// * <pre>
// * {@link AuthorizationEndpoint}:/oauth/authorize请求实现类,第三方请求登录认证
// * {@link TokenEndpoint}:/oauth/token请求的入口点,get和post请求都可以,必须已经请求了/oauth/authorize
// * {@link ClientDetailsService}:读取第三方应用信息
// * ->{@link InMemoryClientDetailsService}:默认实现类,读取第三方应用信息
// * ->{@link JdbcClientDetailsService}:自定义数据库实现类,从数据库读取第三方应用
// * {@link ClientDetails}:根据clientId读取相应的信息,类似于UserDetails,封装第三方应用信息
// * {@link TokenRequest}:封装请求中的其他信息,如用户名,密码等
// * {@link TokenGranter}:4种不同授权模式的实现,根据请求中的grant_type走不同的流程
// * ->{@link CompositeTokenGranter#grant}:集合了4种授权模式的类,并进行验证
// * ->{@link AuthorizationCodeTokenGranter#grant}:授权码模式具体实现类,由CompositeTokenGranter统一调用
// * -->{@link AuthorizationServerTokenServices}:生成OAuth2AccessToken接口
// * --->{@link DefaultTokenServices#createAccessToken()}:根据上一步得到的认证信息,生成OAuth2AccessToken
// * ---->{@link TokenStore}:令牌的存储
// * ---->{@link TokenEnhancer}:令牌增强器,当临牌生成之后,可以对令牌信息进行改造
// * {@link OAuth2AccessToken}:最终的令牌,scope等信息的集合
// * </pre>
// * 
// * SpringOAuth2相关类:
// * 
// * <pre>
// * {@link AuthorizationServerConfigurerAdapter}:继承该类可以对OAuth2登录做些自定义的配置
// * {@link AuthorizationServerEndpointsConfigurer}:SpringOAuth其他入口点配置,如TokenEndpoint
// * </pre>
// *
// * @author 飞花梦影
// * @date 2021-07-02 15:10:26
// * @git {@link https://github.com/dreamFlyingFlower }
// */
//@Configuration
//public class OAuth2JdbcAuthenticationServer extends AuthorizationServerConfigurerAdapter {
//
//	/**
//	 * 授权管理器.不同的版本可能不一样,高版本一般不要.在{@link SecurityConfig#authenticationManagerBean}中设置
//	 */
//	@Autowired
//	private AuthenticationManager authenticationManager;
//
//	// @Autowired
//	// private TokenStore redisTokenStore;
//
//	@Autowired
//	private TokenStore jdbcTokenStore;
//
//	@Autowired
//	private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//	@Autowired
//	private UserApprovalHandler userApprovalHandler;
//
//	/** 用户认证业务 */
//	@Autowired
//	private UserDetailsService userDetailsService;
//
//	@Autowired
//	private AuthorizationCodeServices jdbcAuthorizationCodeServices;
//
//	@Autowired
//	private AuthorizationServerTokenServices jdbcAuthorizationServerTokenServices;
//
//	@Autowired
//	private JdbcClientDetailsService jdbcClientDetailsService;
//
//	/**
//	 * 管理令牌:令牌生成和存储
//	 * 
//	 * 将ClientDetailsServiceConfigurer(从回调AuthorizationServerConfigurer)可以用来在内存或JDBC实现客户的细节服务来定义的
//	 * 
//	 * <pre>
//	 * 客户端的重要属性是:
//	 * clientId:客户端ID,必须
//	 * secret:(可信客户端需要)客户机密码,没有可不填
//	 * scope:客户受限的范围.如果范围未定义或为空(默认),客户端不受范围限制.read write all或其他
//	 * authorizedGrantTypes:授予客户端使用授权的类型,默认值为空
//	 * authorities:授予客户的授权机构(普通的Spring Security权威机构)
//	 * 客户端的详细信息可以通过直接访问底层数据库{@link JdbcClientDetailsService}来更新运行的应用程序
//	 * </pre>
//	 */
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.withClientDetails(jdbcClientDetailsService);
//	}
//
//	/**
//	 * 配置客户端详细信息,客户端标识,客户端秘钥,资源列表等等
//	 * 
//	 * <pre>
//	 * {@link AuthorizationEndpoint}:可以通过以下方式配置支持的授权类型AuthorizationServerEndpointsConfigurer.
//	 * 默认情况下,所有授权类型均受支持,除了密码.以下属性会影响授权类型
//	 * {@link AuthenticationManager}:通过注入密码授权被打开AuthenticationManager
//	 * {@link UserDetailsService}:如果注入UserDetailsService,则刷新令牌授权将包含对用户详细信息的检查,以确保该帐户仍然活动
//	 * {@link AuthorizationCodeServices}:定义AuthorizationCodeServices授权代码授权的授权代码服务
//	 * {@link TokenGranter}:TokenGranter完全控制授予和忽略上述其他属性
//	 * 在XML授予类型中包含作为子元素authorization-server
//	 * 
//	 * /oauth/authorize:可以从该请求中获取所有数据,然后根据需要进行渲染,然后所有用户需要执行的操作都是回复有关批准或拒绝授权的信息
//	 * 请求参数直接传递给UserApprovalHandler,AuthorizationEndpoint
//	 * </pre>
//	 * 
//	 * @param endpoints
//	 * @throws Exception
//	 */
//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints
//				// 用于支持密码模式
//				.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter)
//				.userDetailsService(userDetailsService).userApprovalHandler(userApprovalHandler)
//				// 授权码存储
//				.authorizationCodeServices(jdbcAuthorizationCodeServices)
//				// token服务
//				.tokenServices(jdbcAuthorizationServerTokenServices)
//				// 从数据库查看来源数据
//				.tokenStore(jdbcTokenStore)
//				// 支持的请求类型
//				.allowedTokenEndpointRequestMethods(HttpMethod.POST);
//		// if (null == jwtTokenStore) {
//		// endpoints.tokenStore(redisTokenStore);
//		// } else {
//		// 从jwt查看来源数据
//		// endpoints.tokenStore(jwtTokenStore);
//		// }
//	}
//
//	/**
//	 * 配置令牌端点(Token Endpoint)的安全约束
//	 * 
//	 * <pre>
//	 * /oauth/authorize:授权端点
//	 * /oauth/token:令牌端点
//	 * /oauth/confirm_access:用户确认授权提交端点
//	 * /oauth/error:授权服务错误信息端点
//	 * /oauth/check_token:用于资源服务访问的令牌解析端点
//	 * /oauth/token_key:提供公有密匙的端点,如果使用JWT令牌的话
//	 * </pre>
//	 */
//	@Override
//	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//		// 获得签名的signkey,需要身份验证才行,默认是denyAll(),这是SpringSecurity的权限表达式
//		oauthServer
//				// token_key公开
//				.tokenKeyAccess("permitAll()")
//				// 必须是认证的
//				.checkTokenAccess("isAuthenticated()")
//				// 允许以表单的方式将token传递到服务
//				.allowFormAuthenticationForClients();
//		// oauthServer.tokenKeyAccess("isAnonymous() ||
//		// hasAuthority('ROLE_TRUSTED_CLIENT')");
//		// oauthServer.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
//	}
//}