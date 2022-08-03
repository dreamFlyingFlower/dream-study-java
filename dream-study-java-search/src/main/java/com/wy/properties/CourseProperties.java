package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 课程案例配置参数
 *
 * @author 飞花梦影
 * @date 2022-08-03 16:36:00
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@ConfigurationProperties(prefix = "course")
@Configuration
@Data
public class CourseProperties {

	private String source_field;
}