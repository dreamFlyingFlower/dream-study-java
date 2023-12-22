package com.wy.actuator;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.jmx.annotation.JmxEndpoint;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wy.model.User;

/**
 * 使用相关注解注册自定义Endpoint
 * 
 * <pre>
 * {@link Endpoint}:定义一个监控端点,同时支持 HTTP 和 JMX 两种方式.id作为访问端点的URL,如actuator/cache
 * {@link WebEndpoint}:定义一个监控端点,只支持 HTTP 方式
 * {@link JmxEndpoint}:定义一个监控端点,只支持 JMX 方式
 * {@link ReadOperation}:作用在方法上,可用来返回端点展示的信息(通过 Get 方法请求).如果要根据路径做查询,要用@Selector
 * {@link WriteOperation}:作用在方法上,可用来修改端点展示的信息(通过 Post 方法请求).请求数据必须是json,不能将实体作为参数,要把实体中相应的属性拿出来做参数
 * {@link DeleteOperation}:作用在方法上,可用来删除对应端点信息(通过 Delete 方法请求)
 * {@link Selector}:作用在参数上,用来定位一个端点的具体路由.注意参数的arg0不能改变,改成其他的,开放出去的接口还是/{arg0},还会造成方法无法正常获取参数值
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-12-22 13:55:39
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Endpoint(id = "cache")
public class MyEndpoint {

	private Map<String, User> users = new ConcurrentHashMap<>();

	@ReadOperation
	public Set<String> users() {
		return users.keySet();
	}

	@ReadOperation
	public User usersIdentify(@Selector String arg0) {
		return users.get(arg0);
	}

	@WriteOperation
	public Set<String> set(String userName, String passwd) {
		HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request != null) {
			User user = new User();
			user.setUsername(userName);
			user.setPassword(passwd);
			request.getSession().setAttribute("user", user);

			users.put(user.getUsername(), user);
		}

		return users.keySet();
	}
}