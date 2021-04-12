package com.wy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 分布式session共享配置,使用了redis session之后,原boot的server.session.timeout将被覆盖 FIXME
 * 
 * @author wanyang 2018年7月16日
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfig {

}