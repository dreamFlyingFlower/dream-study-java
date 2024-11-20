package com.wy.signature;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import dream.flying.flower.framework.web.helper.WebHelpers;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.ResultException;
import lombok.extern.slf4j.Slf4j;

/**
 * 验签逻辑不能放在拦截器中,因为拦截器中不能直接读取body的输入流,否则会造成后续@RequestBody的参数解析器读取不到body
 * 由于body输入流只能读取一次,因此需要使用ContentCachingRequestWrapper包装请求,缓存body内容,但是该类的缓存时机是在@RequestBody的参数解析器中
 * 因此,满足2个条件才能获取到ContentCachingRequestWrapper中的body缓存: 接口的入参必须存在@RequestBody
 * 读取body缓存的时机必须在@RequestBody的参数解析之后,比如说:AOP、Controller层的逻辑内.注意拦截器的时机是在参数解析之前的
 * 
 * 综上,标注了@VerifySignature注解的controller层方法的入参必须存在@RequestBody,AOP中验签时才能获取到body的缓存
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:41:59
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ConditionalOnBean(SignatureProperties.class)
@Component
@Slf4j
@Aspect
public class SignatureAspect implements PointCutDef {

	@Resource
	private SignatureManager signatureManager;

	@Pointcut("@annotation(com.wy.signature.VerifySignature)")
	public void annotatedMethod() {
	}

	@Pointcut("@within(com.wy.signature.VerifySignature)")
	public void annotatedClass() {
	}

	@Before("apiMethod() && (annotatedMethod() || annotatedClass())")
	public void verifySignature() {
		HttpServletRequest request = WebHelpers.getRequest();
		String appId = request.getParameter(ConstSignature.REQUEST_PARAM_SIGNATURE_KEY);
		if (StrHelper.isBlank(appId)) {
			throw new ResultException("不受信任的调用者");
		}

		String signature = request.getHeader(ConstSignature.HEADER_SIGNATURE_KEY);
		if (StrHelper.isBlank(signature)) {
			throw new ResultException("无效签名");
		}

		// 提取请求参数
		String requestParamsStr = extractRequestParams(request);
		// 验签
		verifySignature(appId, requestParamsStr, signature);
	}

	public String extractRequestParams(HttpServletRequest request) {
		// @RequestBody
		String body = null;
		// 验签逻辑不能放在拦截器中,因为拦截器中不能直接读取body的输入流,否则会造成后续@RequestBody的参数解析器读取不到body
		// 由于body输入流只能读取一次,因此需要使用ContentCachingRequestWrapper包装请求,缓存body内容,但是该类的缓存时机是在@RequestBody的参数解析器中
		// 因此满足2个条件才能使用ContentCachingRequestWrapper中的body缓存
		// 1. 接口的入参必须存在@RequestBody
		// 2.读取body缓存的时机必须在@RequestBody的参数解析之后,比如说:AOP,Controller层的逻辑内.注意拦截器的时机是在参数解析之前的
		if (request instanceof ContentCachingRequestWrapper) {
			ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
			body = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
		}

		// @RequestParam
		Map<String, String[]> paramMap = request.getParameterMap();

		// @PathVariable
		Map<String, String> pathVariableWebParameter = WebHelpers.getPathVariableWebParameter(request);

		return extractRequestParams(body, paramMap, pathVariableWebParameter);
	}

	/**
	 * 提取所有的请求参数,按照固定规则拼接成一个字符串
	 *
	 * @param body post请求的请求体
	 * @param paramMap 路径参数(QueryString)。形如：name=zhangsan&age=18&label=A&label=B
	 * @param uriTemplateVarNap 路径变量(PathVariable)。形如：/{name}/{age}
	 * @return 所有的请求参数按照固定规则拼接成的一个字符串
	 */
	public String extractRequestParams(@Nullable String body, @Nullable Map<String, String[]> paramMap,
			@Nullable Map<String, String> uriTemplateVarNap) {
		String paramStr = null;
		if (null != paramMap) {
			paramStr = paramMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> {
				// 拷贝一份按字典序升序排序
				String[] sortedValue = Arrays.stream(entry.getValue()).sorted().toArray(String[]::new);
				return entry.getKey() + "=" + StrHelper.join(",", sortedValue);
			}).collect(Collectors.joining("&"));
		}

		String uriVarStr = null;
		if (null != uriTemplateVarNap) {
			uriVarStr = StrHelper.join(",", uriTemplateVarNap.values().stream().sorted().toArray(String[]::new));
		}

		return StrHelper.join("#", body, paramStr, uriVarStr);
	}

	/**
	 * 验证请求参数的签名
	 */
	public void verifySignature(String appId, String requestParamsStr, String signature) {
		try {
			boolean verified = signatureManager.verifySignature(appId, requestParamsStr, signature);
			if (!verified) {
				throw new ResultException("The signature verification result is false.");
			}
		} catch (Exception ex) {
			log.error("Failed to verify signature", ex);
			throw new ResultException("无效签名");
		}
	}
}