package dream.flying.flower.config;

import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 * 新的bean注入方式,但本质上和之前没什么不同,可能是注入的前后顺序不同.仍然需要使用@Import
 *
 * @author 飞花梦影
 * @date 2025-12-18 14:43:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@Import(MyBeansRegistrar.class)
public class BeanRegisterConfig {

}

class MyBeansRegistrar implements BeanRegistrar {

	@Override
	public void register(BeanRegistry registry, Environment env) {
		if (env.matchesProfiles("dev")) {
			registry.registerBean(User.class, spec -> spec.supplier(context -> new User()));
		}
	}
}

class User {

}