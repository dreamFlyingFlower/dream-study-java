package dream.flying.flower.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.reactive.function.client.support.WebClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.HttpServiceGroup.ClientType;
import org.springframework.web.service.registry.ImportHttpServices;

import dream.flying.flower.client.DeviceClient;
import dream.flying.flower.client.UserInternalClient;
import dream.flying.flower.client.UserOfficalClient;
import dream.flying.flower.client.WorkCenterClient;

/**
 * 响应式配置,对应ImportHttpServices#clientType为ClientType#WEB_CLIENT.和RestClientConfig用一个即可
 * 
 * {@link ImportHttpServices}:服务接口,为指定的接口注册 HTTP 代理,分别归属于 branch 和 user 组
 * ->{@link ImportHttpServices#group()}:分组标识,可以认为是某一个服务的标识,所有处于同一个组的配置相同
 * ->{@link ImportHttpServices#types()}:服务接口,默认value()
 * ->{@link ImportHttpServices#clientType()}:设置调用模式,默认RestClient.WEB_CLIENT:响应式调用
 * {@link RestClientHttpServiceGroupConfigurer}:为组内所有服务应用通用配置,如请求头信息
 *
 * @author 飞花梦影
 * @date 2025-12-18 15:07:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration(proxyBeanMethods = false)
@ImportHttpServices(group = "branch", types = { DeviceClient.class, WorkCenterClient.class },
		clientType = ClientType.WEB_CLIENT)
@ImportHttpServices(group = "user", types = { UserInternalClient.class, UserOfficalClient.class })
public class WebClientConfig {

	/**
	 * 对某些配置做统一处理,如打印日志,设置baseurl,请求头等,也可以在配置文件中配置
	 * 
	 * 注意,此处返回的是webClient,要与@ImportHttpServices#clientType=ClientType.WEB_CLIENT对应,如果对应不上,配置不会生效
	 * 
	 * 当前配置只会对branch生效,对user不生效
	 * 
	 * @return RestClientHttpServiceGroupConfigurer
	 */
	@Bean
	WebClientHttpServiceGroupConfigurer webClientConfigurer() {
		return groups -> groups.filterByName("branch", "user")
				.forEachClient((group, builder) -> builder
						// 设置请求地址,也可以写在配置文件spring.http.serviceclient:
						// .baseUrl("http://8.153.96.30:7969/coconut/cummins-loto-branch/cummins")
						.filter((request, next) -> {
							System.out.println("WebClient Request: " + request.method() + " " + request.url());
							return next.exchange(request);
						})
						// 设置统一的User-Agent
						.defaultHeader("User-Agent", "My-Application"));
	}
}