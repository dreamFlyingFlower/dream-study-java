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
	public void onInvokeMethod(ForestRequest request, ForestMethod method, Object[] args) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.onInvokeMethod(request, method, args);
	}

	@Override
	public boolean beforeExecute(ForestRequest request) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.beforeExecute(request);
	}

	@Override
	public void afterExecute(ForestRequest request, ForestResponse response) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.afterExecute(request, response);
	}

	@Override
	public byte[] onBodyEncode(ForestRequest request, ForestEncoder encoder, byte[] encodedData) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.onBodyEncode(request, encoder, encodedData);
	}

	@Override
	public ResponseResult onResponse(ForestRequest request, ForestResponse response) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.onResponse(request, response);
	}

	@Override
	public void onError(ForestRuntimeException ex, ForestRequest request, ForestResponse response) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.onError(ex, request, response);
	}

	@Override
	public void onCanceled(ForestRequest req, ForestResponse res) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.onCanceled(req, res);
	}

	@Override
	public void onRetry(ForestRequest request, ForestResponse response) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.onRetry(request, response);
	}

	@Override
	public void onProgress(ForestProgress progress) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.onProgress(progress);
	}

	@Override
	public void onRedirection(ForestRequest<?> redirectReq, ForestRequest<?> prevReq, ForestResponse<?> prevRes) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.onRedirection(redirectReq, prevReq, prevRes);
	}

	@Override
	public void onLoadCookie(ForestRequest request, ForestCookies cookies) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.onLoadCookie(request, cookies);
	}

	@Override
	public void onSaveCookie(ForestRequest request, ForestCookies cookies) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.onSaveCookie(request, cookies);
	}

	@Override
	public InterceptorAttributes getAttributes(ForestRequest request) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.getAttributes(request);
	}

	@Override
	public void addAttribute(ForestRequest request, String name, Object value) {
		// TODO Auto-generated method stub
		ForestInterceptor.super.addAttribute(request, name, value);
	}

	@Override
	public Object getAttribute(ForestRequest request, String name) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.getAttribute(request, name);
	}

	@Override
	public <R> R getAttribute(ForestRequest request, String name, Class<R> clazz) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.getAttribute(request, name, clazz);
	}

	@Override
	public Boolean getAttributeAsBoolean(ForestRequest request, String name) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.getAttributeAsBoolean(request, name);
	}

	@Override
	public String getAttributeAsString(ForestRequest request, String name) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.getAttributeAsString(request, name);
	}

	@Override
	public Integer getAttributeAsInteger(ForestRequest request, String name) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.getAttributeAsInteger(request, name);
	}

	@Override
	public Float getAttributeAsFloat(ForestRequest request, String name) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.getAttributeAsFloat(request, name);
	}

	@Override
	public Double getAttributeAsDouble(ForestRequest request, String name) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.getAttributeAsDouble(request, name);
	}

	@Override
	public <R> R getOrAddAttribute(ForestRequest request, String name, Supplier<R> supplier) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.getOrAddAttribute(request, name, supplier);
	}

	@Override
	public boolean isGlobalInterceptor(ForestRequest request) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.isGlobalInterceptor(request);
	}

	@Override
	public boolean isBaseInterceptor(ForestRequest request) {
		// TODO Auto-generated method stub
		return ForestInterceptor.super.isBaseInterceptor(request);
	}
}