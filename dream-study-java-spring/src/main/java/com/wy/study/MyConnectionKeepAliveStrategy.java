package com.wy.study;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/**
 * 自定义Httpclient的超时策略,默认是{@link DefaultConnectionKeepAliveStrategy}
 * 
 * @author 飞花梦影
 * @date 2021-10-02 11:05:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

	private static final Integer DEFAULT_TIMEOUT = 30;

	@Override
	public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		Args.notNull(response, "HTTP response");
		final HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		while (it.hasNext()) {
			final HeaderElement he = it.nextElement();
			final String param = he.getName();
			final String value = he.getValue();
			if (value != null && param.equalsIgnoreCase("timeout")) {
				try {
					return Long.parseLong(value) * 1000;
				} catch (final NumberFormatException ignore) {
					return DEFAULT_TIMEOUT * 1000;
				}
			}
		}
		return -1;
	}
}