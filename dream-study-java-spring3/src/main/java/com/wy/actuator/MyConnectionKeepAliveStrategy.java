package com.wy.actuator;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.Args;
import org.apache.hc.core5.util.TimeValue;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义Httpclient的超时策略,默认是{@link DefaultConnectionKeepAliveStrategy}
 * 
 * @author 飞花梦影
 * @date 2021-10-02 11:05:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class MyConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

	private static final Integer DEFAULT_TIMEOUT = 30;

	@Override
	public TimeValue getKeepAliveDuration(HttpResponse response, HttpContext context) {
		Args.notNull(response, "HTTP response");
		final BasicHeaderElementIterator it =
				new BasicHeaderElementIterator(response.headerIterator(HttpHeaders.KEEP_ALIVE));
		while (it.hasNext()) {
			final HeaderElement he = it.next();
			final String param = he.getName();
			final String value = he.getValue();
			if (value != null && param.equalsIgnoreCase("timeout")) {
				try {
					return TimeValue.ofMilliseconds(Long.parseLong(value) * 1000L);
				} catch (final NumberFormatException ignore) {
					log.error("ignore timeout exception");
					return TimeValue.ofMilliseconds(DEFAULT_TIMEOUT * 1000L);
				}
			}
		}
		return TimeValue.ofMilliseconds(DEFAULT_TIMEOUT * 1000L);
	}
}