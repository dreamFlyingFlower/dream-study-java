package dream.study.authorization.server.core;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

/**
 * 设备参数,参照高版本{@link OAuth2ParameterNames},高版本中已经存在该参数
 *
 * @author 飞花梦影
 * @date 2024-09-19 14:52:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface CustomizerOAuth2ParameterNames extends OAuth2ParameterNames {

	/**
	 * {@code device_code} - used in Device Authorization Response and Device Access
	 * Token Request.
	 * 
	 * @since 6.1
	 */
	String DEVICE_CODE = "device_code";

	/**
	 * {@code user_code} - used in Device Authorization Response.
	 * 
	 * @since 6.1
	 */
	String USER_CODE = "user_code";

	/**
	 * {@code verification_uri} - used in Device Authorization Response.
	 * 
	 * @since 6.1
	 */
	String VERIFICATION_URI = "verification_uri";

	/**
	 * {@code verification_uri_complete} - used in Device Authorization Response.
	 * 
	 * @since 6.1
	 */
	String VERIFICATION_URI_COMPLETE = "verification_uri_complete";

	/**
	 * {@code interval} - used in Device Authorization Response.
	 * 
	 * @since 6.1
	 */
	String INTERVAL = "interval";
}