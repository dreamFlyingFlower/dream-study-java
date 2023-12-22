package com.wy.dynamicdb;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2023-12-22 17:40:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("config.ds")
public class DsMultiProperties {

	private List<DruidDataSourceWrapper> druidDataSources;
}