package dream.study.authorization.server.core;

import java.io.Serializable;

import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

/**
 * 设备认证类型, 参照{@link AuthorizationGrantType},在3.xx版本中,该方式已存在
 *
 * @author 飞花梦影
 * @date 2024-09-19 14:47:45
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class CustomizerAuthorizationGrantType implements Serializable {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	public static final AuthorizationGrantType DEVICE_CODE =
			new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:device_code");

	private final String value;

	/**
	 * Constructs an {@code AuthorizationGrantType} using the provided value.
	 * 
	 * @param value the value of the authorization grant type
	 */
	public CustomizerAuthorizationGrantType(String value) {
		Assert.hasText(value, "value cannot be empty");
		this.value = value;
	}

	/**
	 * Returns the value of the authorization grant type.
	 * 
	 * @return the value of the authorization grant type
	 */
	public String getValue() {
		return this.value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		AuthorizationGrantType that = (AuthorizationGrantType) obj;
		return this.getValue().equals(that.getValue());
	}

	@Override
	public int hashCode() {
		return this.getValue().hashCode();
	}
}