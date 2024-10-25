package dream.study.authorization.server.provider.device;

import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * 设备码模式token
 *
 * @author 飞花梦影
 * @date 2024-09-18 22:08:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Transient
public class DeviceClientAuthenticationToken extends OAuth2ClientAuthenticationToken {

	private static final long serialVersionUID = -1097179551957918845L;

	public DeviceClientAuthenticationToken(String clientId, ClientAuthenticationMethod clientAuthenticationMethod,
			@Nullable Object credentials, @Nullable Map<String, Object> additionalParameters) {
		super(clientId, clientAuthenticationMethod, credentials, additionalParameters);
	}

	public DeviceClientAuthenticationToken(RegisteredClient registeredClient,
			ClientAuthenticationMethod clientAuthenticationMethod, @Nullable Object credentials) {
		super(registeredClient, clientAuthenticationMethod, credentials);
	}
}