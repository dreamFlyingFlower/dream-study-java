package dream.study.authorization.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.nimbusds.jose.jwk.source.JWKSource;

import dream.study.authorization.server.config.AuthorizationServerConfig;

/**
 * SpringSecurity认证服务器,5.7以上版本已经抛弃了EnableAuthorizationServer等相关注解,直接使用拦截器SecurityFilterChain
 * 
 * Spring Authorization Server
 * 
 * <pre>
 * 是Spring Security OAuth的进化版本,引入了对OAuth 2.1和OpenID Connect 1.0规范的支持.
 * 基于Spring Security,为构建OpenID Connect 1.0身份提供者和OAuth2授权服务器产品提供了安全、轻量级和可定制的基础.
 * OAuth 2.1和OpenID Connect 1.0是用于身份验证和授权的行业标准协议,被广泛应用于各种应用程序和系统,以实现安全的用户身份验证和授权流程.
 * </pre>
 * 
 * spring-security-oauth2-authorization-server的JAR包下有多个SQL文件
 * 
 * <pre>
 * oauth2-registered-client-schema.sql:已注册的客户端信息表,操作类为 JdbcRegisteredClientRepository
 * oauth2-authorization-consent-schema.sql:认证授权表,操作类为 JdbcOAuth2AuthorizationConsentService
 * oauth2-authorization-schema.sql:认证信息表,操作类为 JdbcOAuth2AuthorizationService
 * </pre>
 * 
 * 相关类,eg:{@link AuthorizationServerConfig}
 * 
 * <pre>
 * {@link AbstractAuthenticationProcessingFilter#doFilter}:与认证相关的拦截器从此处触发
 * 
 * {@link RegisteredClientRepository}:认证的客户端数据操作,自定义操作需实现该接口
 * {@link JdbcRegisteredClientRepository}:数据库认证客户端操作
 * {@link InMemoryRegisteredClientRepository}:内存认证客户端操作,可直接在配置文件中编写
 * 
 * {@link AuthorizationServerSettings}:认证服务器相关设置
 * 
 * {@link BearerTokenAuthenticationFilter}:Bearer Token拦截器
 * 
 * {@link OAuth2AuthorizationService}:OAuth2认证服务接口,自定义操作需实现该接口
 * {@link JdbcOAuth2AuthorizationService}:基于数据库的OAuth2认证服务
 * {@link InMemoryOAuth2AuthorizationService}:基于内存的OAuth2认证服务
 * 
 * {@link OAuth2AuthorizationConsentService}:授权确认管理服务,自定义操作需实现该接口
 * {@link JdbcOAuth2AuthorizationConsentService}:基于数据库的授权确认管理服务
 * {@link InMemoryOAuth2AuthorizationConsentService}:基于内存的授权确认管理服务
 * 
 * {@link JWKSource}:JWK是一种JSON格式的密钥表示,用于描述加密算法使用的密钥.JWT使用JWK签名和验签,确保令牌的真实和完整性
 * </pre>
 * 
 * 程序启动后,有以下几个固定访问端点:
 * 
 * <pre>
 * 访问登录页面:GET:http://localhost:17127/oauth/authorize?client_id=test-client&response_type=code&scope=user&redirect_uri=https://www.baidu.com
 * 登录成功跳转授权页面
 * 授权完成后跳转了认证成功的回调地址,上述地址为https://www.baidu.com
 * 
 * 获取到授权码就可以访问/oauth/token获取JWT token了
 * 请求访问令牌:POST:http://localhost:17127/oauth/token?grant_type=code&client_id=test-client&client_secret=your_client_secret
 * 
 * 使用访问令牌访问受保护的资源:GET:http://localhost:17127/user?access_token=your_access_token
 * </pre>
 * 
 * 客户端向其他认证服务器进行认证:
 * 
 * <pre>
 * {@link RegisteredOAuth2AuthorizedClient}:注解,使用方式见该文件.直接使用客户端配置
 * 
 * {@link JdbcOAuth2AuthorizedClientService}:配合{@link JdbcRegisteredClientRepository}使用Jdbc存储客户端Token信息
 * </pre>
 * 
 * spring-security-oauth2-client的JAR包下有多个SQL文件
 * 
 * <pre>
 * oauth2-client-schema.sql:认证服务器签发的配置文件,操作类为 JdbcOAuth2AuthorizedClientService
 * oauth2-client-schema-postgres.sql:认证服务器签发的配置文件
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2022-05-15 18:46:46
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@SpringBootApplication
public class AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}
}