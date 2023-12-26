package com.wy.signature;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.wy.lang.StrHelper;
import com.wy.result.ResultException;

import dream.framework.web.helper.WebHelpers;
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

	@Pointcut("@annotation(top.ysqorz.signature.enumeration.VerifySignature)")
	public void annotatedMethod() {
	}

	@Pointcut("@within(top.ysqorz.signature.enumeration.VerifySignature)")
	public void annotatedClass() {
	}

	@Before("apiMethod() && (annotatedMethod() || annotatedClass())")
	public void verifySignature() {
		HttpServletRequest request = WebHelpers.getRequest();
		String callerID = request.getParameter(ConstSignature.REQUEST_PARAM_SIGNATURE_KEY);
		if (StrHelper.isBlank(callerID)) {
			throw new ResultException("不受信任的调用者");
		}

		// 从请求头中提取签名,不存在直接驳回
		String signature = request.getHeader(ConstSignature.HEADER_SIGNATURE_KEY);
		if (StrHelper.isBlank(signature)) {
			throw new ResultException("无效签名");
		}

		// 提取请求参数
		String requestParamsStr = extractRequestParams(request);
		// 验签,验签不通过抛出业务异常
		verifySignature(callerID, requestParamsStr, signature);
	}

	@SuppressWarnings("unchecked")
	public String extractRequestParams(HttpServletRequest request) {
		// @RequestBody
		String body = null;
		// 验签逻辑不能放在拦截器中,因为拦截器中不能直接读取body的输入流,否则会造成后续@RequestBody的参数解析器读取不到body
		// 由于body输入流只能读取一次,因此需要使用ContentCachingRequestWrapper包装请求,缓存body内容,但是该类的缓存时机是在@RequestBody的参数解析器中
		// 因此满足2个条件才能使用ContentCachingRequestWrapper中的body缓存
		// 1. 接口的入参必须存在@RequestBody
		// 2.读取body缓存的时机必须在@RequestBody的参数解析之后,比如说：AOP、Controller层的逻辑内.注意拦截器的时机是在参数解析之前的
		if (request instanceof ContentCachingRequestWrapper) {
			ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
			body = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
		}

		// @RequestParam
		Map<String, String[]> paramMap = request.getParameterMap();

		// @PathVariable
		ServletWebRequest webRequest = new ServletWebRequest(request, null);
		Map<String, String> uriTemplateVarNap = (Map<String, String>) webRequest
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);

		return SignatureHelpers.extractRequestParams(body, paramMap, uriTemplateVarNap);
	}

	/**
	 * 验证请求参数的签名
	 */
	public void verifySignature(String callerID, String requestParamsStr, String signature) {
		try {
			boolean verified = signatureManager.verifySignature(callerID, requestParamsStr, signature);
			if (!verified) {
				throw new ResultException("The signature verification result is false.");
			}
		} catch (Exception ex) {
			log.error("Failed to verify signature", ex);
			throw new ResultException("无效签名");
		}
	}
}