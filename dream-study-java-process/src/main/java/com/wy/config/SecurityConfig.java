//package com.wy.config;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 配合SpringSecurity使用,添加具有相关权限的用户
// * 
// * @author 飞花梦影
// * @date 2021-08-17 23:30:22
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//@Configuration
//@EnableWebSecurity
//@Slf4j
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Override
//	@Autowired
//	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(myUserDetailsService());
//	}
//
//	@Bean
//	public UserDetailsService myUserDetailsService() {
//
//		InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//
//		String[][] usersGroupsAndRoles = { { "test1", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam" },
//				{ "test2", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam" },
//				{ "test3", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam" },
//				{ "test4", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam" },
//				{ "admin", "password", "ROLE_ACTIVITI_ADMIN" } };
//
//		for (String[] user : usersGroupsAndRoles) {
//			List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length));
//			log.info("> Registering new user: " + user[0] + " with the following Authorities[" + authoritiesStrings
//					+ "]");
//			inMemoryUserDetailsManager.createUser(new User(user[0], passwordEncoder().encode(user[1]),
//					authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList())));
//		}
//
//		return inMemoryUserDetailsManager;
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//}