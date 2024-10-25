package dream.study.authorization.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import dream.study.authorization.server.helpers.SecurityContextOAuth2Helpers;

/**
 * SpringSecurity5.8.14资源服务器配置
 *
 * @author 飞花梦影
 * @date 2024-09-18 22:02:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class ResourceServerConfig {

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				// 被放行的接口上不能有权限注解,如@PreAuthorize,否则无效
				.anyRequest()
				.authenticated())
				.oauth2ResourceServer(resourceServer -> resourceServer
						// 可在此处添加自定义解析设置
						.jwt(Customizer.withDefaults())
						// 添加未携带token和权限不足异常处理(已在第五篇文章中说过)
						.accessDeniedHandler(SecurityContextOAuth2Helpers::exceptionHandler)
						.authenticationEntryPoint(SecurityContextOAuth2Helpers::exceptionHandler));
		return http.build();
	}
}