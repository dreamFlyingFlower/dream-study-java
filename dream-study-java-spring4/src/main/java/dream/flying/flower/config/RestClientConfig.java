package dream.flying.flower.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.registry.ImportHttpServices;

import dream.flying.flower.client.DeviceClient;
import dream.flying.flower.client.WorkCenterClient;
import dream.flying.flower.entity.User;

/**
 * 阻塞式配置,对应ImportHttpServices#clientType为默认值或ClientType#REST_CLIENT.和WebClientConfig用一个即可
 * 
 * {@link ImportHttpServices}:为指定的接口注册 HTTP 代理,分别归属于 branch 和 user 组
 * {@link ImportHttpServices#group()}:分组标识,可以认为是某一个服务的标识,所有处于同一个组的配置相同
 * {@link ImportHttpServices#types()}:服务接口,默认value()
 * {@link ImportHttpServices()}:服务接口
 * {@link ImportHttpServices#clientType()}:设置调用模式,默认REST_CLIENT.WEB_CLIENT:响应式调用
 * {@link RestClientHttpServiceGroupConfigurer}:为组内所有服务应用通用配置,如请求头信息
 *
 * @author 飞花梦影
 * @date 2025-12-18 15:07:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration(proxyBeanMethods = false)
@ImportHttpServices(group = "branch", types = { DeviceClient.class, WorkCenterClient.class })
@ImportHttpServices(group = "user", types = { UserServiceInternal.class, UserServiceOfficial.class })
public class RestClientConfig {

	/**
	 * 对某些配置做统一处理,如打印日志,设置baseurl,请求头等,也可以在配置文件中配置
	 * 
	 * 注意,此处返回的是restClient,要与@ImportHttpServices#clientType默认值或ClientType.REST_CLIENT对应,如果对应不上,配置不会生效
	 * 
	 * @return RestClientHttpServiceGroupConfigurer
	 */
	@Bean
	RestClientHttpServiceGroupConfigurer restClientConfigurer() {
		return groups -> groups.filterByName("branch", "user")
				.forEachClient((group, builder) -> builder
						// 设置请求地址,也可以写在配置文件spring.http.serviceclient:
						// .baseUrl("http://8.153.96.30:7969/coconut/cummins-loto-branch/cummins")
						.requestInterceptor((request, body, execution) -> {
							// 添加日志拦截器
							System.out.println("Request: " + request.getMethod() + " " + request.getURI());
							return execution.execute(request, body);
						})
						// 设置统一的User-Agent
						.defaultHeader("User-Agent", "My-Application"));
	}
}

@HttpExchange("/user")
interface UserServiceInternal {

	@GetExchange
	String getUserDetails();

	@PostExchange
	User createUser(@RequestBody User user);
}

interface UserServiceOfficial {

	String getOfficialUserData();
}