package com.wy.model.alipay;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-28 18:06:38
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlipayExtendParams {

	@JsonAlias("sys_service_provider_id")
	private String sysServiceProviderId;
}