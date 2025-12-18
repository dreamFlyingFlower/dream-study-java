package dream.flying.flower.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.AbstractHttpServiceRegistrar;
import org.springframework.web.service.registry.ImportHttpServices;

/**
 * {@link ImportHttpServices}:为指定的接口注册 HTTP 代理,分别归属于 weather 和 user 组
 * {@link RestClientHttpServiceGroupConfigurer}:为组内所有服务应用通用配置,如请求头信息
 *
 * @author 飞花梦影
 * @date 2025-12-18 15:07:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration(proxyBeanMethods = false)
@ImportHttpServices(group = "weather", types = { FreeWeather.class, CommercialWeather.class })
@ImportHttpServices(group = "user", types = { UserServiceInternal.class, UserServiceOfficial.class })
public class ImportHttpServicesConfig extends AbstractHttpServiceRegistrar {

	@Bean
	RestClientHttpServiceGroupConfigurer groupConfigurer() {
		return groups -> groups.filterByName("weather", "user")
				.forEachClient((group, builder) -> builder.defaultHeader("User-Agent", "My-Application"));
	}

	@Override
	protected void registerHttpServices(GroupRegistry registry, AnnotationMetadata importingClassMetadata) {
		// TODO Auto-generated method stub
	}
}

interface FreeWeather {

	String getForecast();
}

interface CommercialWeather {

	String getDetailedForecast();
}

interface UserServiceInternal {

	String getUserDetails();
}

interface UserServiceOfficial {

	String getOfficialUserData();
}