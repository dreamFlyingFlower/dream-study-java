package dream.study.spring.apicontrol.path;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import dream.study.spring.apicontrol.ApiVersion;
import lombok.Getter;

/**
 * 基于URL路径实现的版本控制:当前类控制request指向哪个method
 *
 * @author 飞花梦影
 * @date 2023-10-12 17:04:45
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Getter
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

	/** 版本正则表达式,注意不能为参数重复 */
	private static final Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+\\.\\d+)");

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
	public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
		return 0;
		// 优先匹配最新的版本号,和getMatchingCondition注释掉的代码同步使用
		// return other.getVersion().compareTo(this.version);
	}

	@Override
	public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
		Matcher m = VERSION_PREFIX_PATTERN.matcher(httpServletRequest.getRequestURI());
		if (m.find()) {
			String pathVersion = m.group(1);
			// 这个方法是精确匹配
			if (Objects.equals(pathVersion, apiVersion.value())) {
				return this;
			}
			// 该方法是只要大于等于最低接口version即匹配成功,需要和compareTo()配合
			// 举例：定义有1.0/1.1接口,访问1.2,则实际访问的是1.1,如果从小开始那么排序反转即可
			// if(Float.parseFloat(pathVersion)>=Float.parseFloat(version)){
			// return this;
			// }
		}
		return null;
	}
}