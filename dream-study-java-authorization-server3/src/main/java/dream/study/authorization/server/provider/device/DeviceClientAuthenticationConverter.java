package dream.study.authorization.server.provider.device;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import dream.study.authorization.server.core.CustomizerAuthorizationGrantType;
import dream.study.authorization.server.core.CustomizerOAuth2ParameterNames;

/**
 * 获取请求中参数转化为DeviceClientAuthenticationToken
 *
 * @author 飞花梦影
 * @date 2024-09-18 22:07:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public final class DeviceClientAuthenticationConverter implements AuthenticationConverter {

	private final RequestMatcher deviceAuthorizationRequestMatcher;

	private final RequestMatcher deviceAccessTokenRequestMatcher;

	public DeviceClientAuthenticationConverter(String deviceAuthorizationEndpointUri) {
		RequestMatcher clientIdParameterMatcher =
				request -> request.getParameter(OAuth2ParameterNames.CLIENT_ID) != null;
		this.deviceAuthorizationRequestMatcher =
				new AndRequestMatcher(new AntPathRequestMatcher(deviceAuthorizationEndpointUri, HttpMethod.POST.name()),
						clientIdParameterMatcher);
		this.deviceAccessTokenRequestMatcher = request -> CustomizerAuthorizationGrantType.DEVICE_CODE.getValue()
				.equals(request.getParameter(CustomizerOAuth2ParameterNames.GRANT_TYPE))
				&& request.getParameter(CustomizerOAuth2ParameterNames.DEVICE_CODE) != null
				&& request.getParameter(CustomizerOAuth2ParameterNames.CLIENT_ID) != null;
	}

	@Nullable
	@Override
	public Authentication convert(HttpServletRequest request) {
		if (!this.deviceAuthorizationRequestMatcher.matches(request)
				&& !this.deviceAccessTokenRequestMatcher.matches(request)) {
			return null;
		}

		// client_id (REQUIRED)
		String clientId = request.getParameter(OAuth2ParameterNames.CLIENT_ID);
		if (!StringUtils.hasText(clientId) || request.getParameterValues(OAuth2ParameterNames.CLIENT_ID).length != 1) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
		}

		return new DeviceClientAuthenticationToken(clientId, ClientAuthenticationMethod.NONE, null, null);
	}
}