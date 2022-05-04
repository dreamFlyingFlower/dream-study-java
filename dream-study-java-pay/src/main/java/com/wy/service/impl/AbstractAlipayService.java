package com.wy.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.wy.request.alipay.AbstractAlipayDealRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-29 09:13:44
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public abstract class AbstractAlipayService {

	AbstractAlipayService() {
	}

	protected void validateBuilder(AbstractAlipayDealRequest builder) {
		if (builder == null) {
			throw new NullPointerException("builder should not be NULL!");
		} else if (!builder.validate()) {
			throw new IllegalStateException("builder validate failed! " + builder.toString());
		}
	}

	protected AlipayResponse getResponse(AlipayClient client, AlipayRequest<? extends AlipayResponse> request) {
		try {
			AlipayResponse response = client.execute(request);
			if (response != null) {
				log.info(response.getBody());
			}
			return response;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
	}
}