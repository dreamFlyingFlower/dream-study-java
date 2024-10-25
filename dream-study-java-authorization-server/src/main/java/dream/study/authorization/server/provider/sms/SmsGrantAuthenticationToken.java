package dream.study.authorization.server.provider.sms;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * 自定义短信验证登录Token类
 *
 * @author 飞花梦影
 * @date 2024-09-18 22:18:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SmsGrantAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 8766937495157741790L;

	/**
	 * 本次登录申请的scope
	 */
	private final Set<String> scopes;

	/**
	 * 客户端认证信息
	 */
	private final Authentication clientPrincipal;

	/**
	 * 当前请求的参数
	 */
	private final Map<String, Object> additionalParameters;

	/**
	 * 认证方式
	 */
	private final AuthorizationGrantType authorizationGrantType;

	public SmsGrantAuthenticationToken(AuthorizationGrantType authorizationGrantType,
			Authentication clientPrincipal, @Nullable Set<String> scopes,
			@Nullable Map<String, Object> additionalParameters) {
		super(Collections.emptyList());
		this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
		this.clientPrincipal = clientPrincipal;
		this.additionalParameters = Collections.unmodifiableMap(
				additionalParameters != null ? new HashMap<>(additionalParameters) : Collections.emptyMap());
		this.authorizationGrantType = authorizationGrantType;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return clientPrincipal;
	}

	/**
	 * 返回请求的scope(s)
	 *
	 * @return 请求的scope(s)
	 */
	public Set<String> getScopes() {
		return this.scopes;
	}

	/**
	 * 返回请求中的authorization grant type
	 *
	 * @return authorization grant type
	 */
	public AuthorizationGrantType getAuthorizationGrantType() {
		return this.authorizationGrantType;
	}

	/**
	 * 返回请求中的附加参数
	 *
	 * @return 附加参数
	 */
	public Map<String, Object> getAdditionalParameters() {
		return this.additionalParameters;
	}
}