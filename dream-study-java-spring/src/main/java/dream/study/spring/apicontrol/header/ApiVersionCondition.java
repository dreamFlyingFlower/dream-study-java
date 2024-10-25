package dream.study.spring.apicontrol.header;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import dream.study.spring.apicontrol.ApiVersion;
import lombok.Getter;

/**
 * 基于请求头控制API版本,需要在请求头添加X-VERSION
 *
 * @author 飞花梦影
 * @date 2023-10-12 17:15:20
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Getter
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

	private static final String X_VERSION = "X-VERSION";

	private final ApiVersion apiVersion;

	public ApiVersionCondition(ApiVersion apiVersion) {
		this.apiVersion = apiVersion;
	}

	@Override
	public ApiVersionCondition combine(ApiVersionCondition other) {
		// 采用最后定义优先原则,则方法上的定义覆盖类上面的定义
		return new ApiVersionCondition(other.getApiVersion());
	}

	@Override
	public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
		String headerVersion = httpServletRequest.getHeader(X_VERSION);
		if (Objects.equals(apiVersion.value(), headerVersion)) {
			return this;
		}
		return null;
	}

	@Override
	public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
		return 0;
	}
}