package com.wy.forest;

import java.util.function.Supplier;

import com.dtflys.forest.converter.ForestEncoder;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestCookies;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.ForestInterceptor;
import com.dtflys.forest.interceptor.InterceptorAttributes;
import com.dtflys.forest.interceptor.ResponseResult;
import com.dtflys.forest.reflection.ForestMethod;
import com.dtflys.forest.utils.ForestProgress;

/**
 * Forest拦截器
 *
 * @author 飞花梦影
 * @date 2025-12-23 15:41:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MyForestInteceptor implements ForestInterceptor {

	@Override
	public void onInvokeMethod(@SuppressWarnings("rawtypes") ForestRequest request,
			@SuppressWarnings("rawtypes") ForestMethod method, Object[] args) {
	}

	@Override
	public boolean beforeExecute(@SuppressWarnings("rawtypes") ForestRequest request) {
		return true;
	}

	@Override
	public void afterExecute(@SuppressWarnings("rawtypes") ForestRequest request,
			@SuppressWarnings("rawtypes") ForestResponse response) {
	}

	@Override
	public byte[] onBodyEncode(@SuppressWarnings("rawtypes") ForestRequest request, ForestEncoder encoder,
			byte[] encodedData) {
		return encodedData;
	}

	@Override
	public ResponseResult onResponse(@SuppressWarnings("rawtypes") ForestRequest request,
			@SuppressWarnings("rawtypes") ForestResponse response) {
		return ResponseResult.RESPONSE_RESULT_PROCEED;
	}

	@Override
	public void onError(ForestRuntimeException ex, @SuppressWarnings("rawtypes") ForestRequest request,
			@SuppressWarnings("rawtypes") ForestResponse response) {
	}

	@Override
	public void onCanceled(@SuppressWarnings("rawtypes") ForestRequest req,
			@SuppressWarnings("rawtypes") ForestResponse res) {
	}

	@Override
	public void onRetry(@SuppressWarnings("rawtypes") ForestRequest request,
			@SuppressWarnings("rawtypes") ForestResponse response) {
	}

	@Override
	public void onProgress(ForestProgress progress) {
	}

	@Override
	public void onRedirection(ForestRequest<?> redirectReq, ForestRequest<?> prevReq, ForestResponse<?> prevRes) {
	}

	@Override
	public void onLoadCookie(@SuppressWarnings("rawtypes") ForestRequest request, ForestCookies cookies) {
	}

	@Override
	public void onSaveCookie(@SuppressWarnings("rawtypes") ForestRequest request, ForestCookies cookies) {
	}

	@Override
	public InterceptorAttributes getAttributes(@SuppressWarnings("rawtypes") ForestRequest request) {
		return request.getInterceptorAttributes(this.getClass());
	}

	@Override
	public void addAttribute(@SuppressWarnings("rawtypes") ForestRequest request, String name, Object value) {
		request.addInterceptorAttribute(this.getClass(), name, value);
	}

	@Override
	public Object getAttribute(@SuppressWarnings("rawtypes") ForestRequest request, String name) {
		return request.getInterceptorAttribute(this.getClass(), name);
	}

	@Override
	public <R> R getAttribute(@SuppressWarnings("rawtypes") ForestRequest request, String name, Class<R> clazz) {
		Object obj = request.getInterceptorAttribute(this.getClass(), name);
		if (obj == null) {
			return null;
		}
		return clazz.cast(obj);
	}

	@Override
	public Boolean getAttributeAsBoolean(@SuppressWarnings("rawtypes") ForestRequest request, String name) {
		Object attr = getAttribute(request, name);
		if (attr == null) {
			return null;
		}
		if (attr instanceof Boolean) {
			return (Boolean) attr;
		}
		return Boolean.valueOf(String.valueOf(attr));
	}

	@Override
	public String getAttributeAsString(@SuppressWarnings("rawtypes") ForestRequest request, String name) {
		Object attr = getAttribute(request, name);
		if (attr == null) {
			return null;
		}
		return String.valueOf(attr);
	}

	@Override
	public Integer getAttributeAsInteger(@SuppressWarnings("rawtypes") ForestRequest request, String name) {
		Object attr = getAttribute(request, name);
		if (attr == null) {
			return null;
		}
		return (Integer) attr;
	}

	@Override
	public Float getAttributeAsFloat(@SuppressWarnings("rawtypes") ForestRequest request, String name) {
		Object attr = getAttribute(request, name);
		if (attr == null) {
			return null;
		}
		return (Float) attr;
	}

	@Override
	public Double getAttributeAsDouble(@SuppressWarnings("rawtypes") ForestRequest request, String name) {
		Object attr = getAttribute(request, name);
		if (attr == null) {
			return null;
		}
		return (Double) attr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R getOrAddAttribute(@SuppressWarnings("rawtypes") ForestRequest request, String name,
			Supplier<R> supplier) {
		Object obj = getAttribute(request, name);
		if (obj == null && supplier != null) {
			obj = supplier.get();
			addAttribute(request, name, obj);
		}
		return (R) obj;
	}

	@Override
	public boolean isGlobalInterceptor(@SuppressWarnings("rawtypes") ForestRequest request) {
		Boolean ret = getAttributeAsBoolean(request, "__global__");
		return ret != null && ret;
	}

	@Override
	public boolean isBaseInterceptor(@SuppressWarnings("rawtypes") ForestRequest request) {
		Boolean ret = getAttributeAsBoolean(request, "__base__");
		return ret != null && ret;
	}
}