package com.wy.config;

import java.util.Collection;

import org.activiti.runtime.api.impl.ProcessRuntimeImpl;
import org.activiti.runtime.api.impl.TaskRuntimeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import lombok.extern.slf4j.Slf4j;

/**
 * Activiti整合SpringSecurity,需要设置相关参数,模拟调用SpringSecurity登录鉴权
 * 
 * {@link TaskRuntimeImpl}和{@link ProcessRuntimeImpl}都需要鉴权
 * 
 * @author 飞花梦影
 * @date 2021-08-17 23:15:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
@Slf4j
public class SecurityActivitiConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	public void logInAs(String username) {
		UserDetails user = userDetailsService.loadUserByUsername(username);
		if (user == null) {
			throw new IllegalStateException("User " + username + " doesn't exist, please provide a valid user");
		}
		log.info("> Logged in as: " + username);
		SecurityContextHolder.setContext(new SecurityContextImpl(new Authentication() {

			private static final long serialVersionUID = -435706064717094771L;

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return user.getAuthorities();
			}

			@Override
			public Object getCredentials() {
				return user.getPassword();
			}

			@Override
			public Object getDetails() {
				return user;
			}

			@Override
			public Object getPrincipal() {
				return user;
			}

			@Override
			public boolean isAuthenticated() {
				return true;
			}

			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

			}

			@Override
			public String getName() {
				return user.getUsername();
			}
		}));
		org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(username);
	}
}