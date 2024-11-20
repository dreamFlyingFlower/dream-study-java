package com.wy.apicontrol.header;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 基于Header实现的版本控制:注入HeaderVersion控制器
 *
 * @author 飞花梦影
 * @date 2023-10-12 17:13:16
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Configuration
public class ApiVersionConfig implements WebMvcRegistrations {

	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new HeaderVersionHandlerMapping();
	}

}