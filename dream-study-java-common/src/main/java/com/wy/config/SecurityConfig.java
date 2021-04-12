package com.wy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 启用security安全类,需要重写WebSecurityConfigurerAdapter类
 * 
 * @author wanyang 2018年7月18日
 * 
 *         FIXME 太复杂
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 定义安全策略
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.authorizeRequests().antMatchers("/", "/login",
		// "account").permitAll().anyRequest().authenticated().and()
		// .logout().permitAll().and().formLogin();
		// http.csrf();
	}
}