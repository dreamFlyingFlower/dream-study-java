package dream.study.authorization.server.context;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import dream.flying.flower.autoconfigure.redis.helper.RedisHelpers;
import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.security.constant.ConstAuthorization;
import dream.study.authorization.server.constant.ConstAuthorizationServerRedis;
import lombok.RequiredArgsConstructor;

/**
 * 基于redis存储认证信息
 * 
 * 参照{@link DelegatingSecurityContextRepository}
 *
 * @author 飞花梦影
 * @date 2024-09-18 22:33:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@SuppressWarnings("deprecation")
@Component
@RequiredArgsConstructor
public class RedisSecurityContextRepository implements SecurityContextRepository {

	private final RedisHelpers redisHelpers;

	private final SecurityContextHolderStrategy securityContextHolderStrategy =
			SecurityContextHolder.getContextHolderStrategy();

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		// 方法已过时,使用 loadDeferredContext 方法
		HttpServletRequest request = requestResponseHolder.getRequest();
		return loadDeferredContext(request).get();
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		String nonce = getNonce(request);
		if (ObjectUtils.isEmpty(nonce)) {
			return;
		}

		// 如果当前的context是空的,则移除
		SecurityContext emptyContext = this.securityContextHolderStrategy.createEmptyContext();
		if (emptyContext.equals(context)) {
			redisHelpers.delete(ConstAuthorizationServerRedis.SECURITY_CONTEXT_PREFIX_KEY + nonce);
		} else {
			// 保存认证信息
			redisHelpers.setExpire((ConstAuthorizationServerRedis.SECURITY_CONTEXT_PREFIX_KEY + nonce),
					JsonHelpers.toString(context), ConstAuthorizationServerRedis.DEFAULT_TIMEOUT_SECONDS);
		}
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		String nonce = getNonce(request);
		if (ObjectUtils.isEmpty(nonce)) {
			return false;
		}
		// 检验当前请求是否有认证信息
		return redisHelpers.get((ConstAuthorizationServerRedis.SECURITY_CONTEXT_PREFIX_KEY + nonce)) != null;
	}

	@Override
	public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
		Supplier<SecurityContext> supplier = () -> readSecurityContextFromRedis(request);
		return new SupplierDeferredSecurityContext(supplier, this.securityContextHolderStrategy);
	}

	/**
	 * 从redis中获取认证信息
	 *
	 * @param request 当前请求
	 * @return 认证信息
	 */
	private SecurityContext readSecurityContextFromRedis(HttpServletRequest request) {
		if (request == null) {
			return null;
		}

		String nonce = getNonce(request);
		if (ObjectUtils.isEmpty(nonce)) {
			return null;
		}

		// 根据缓存id获取认证信息
		return JsonHelpers.read(redisHelpers.get(ConstAuthorizationServerRedis.SECURITY_CONTEXT_PREFIX_KEY + nonce),
				SecurityContextImpl.class);
	}

	/**
	 * 先从请求头中找,找不到去请求参数中找,找不到获取当前session的id 2023-07-11新增逻辑：获取当前session的sessionId
	 *
	 * @param request 当前请求
	 * @return 随机字符串(sessionId),这个字符串本来是前端生成,现在改为后端获取的sessionId
	 */
	private String getNonce(HttpServletRequest request) {
		String nonce = request.getHeader(ConstAuthorization.NONCE_HEADER_NAME);
		if (ObjectUtils.isEmpty(nonce)) {
			nonce = request.getParameter(ConstAuthorization.NONCE_HEADER_NAME);
			HttpSession session = request.getSession(Boolean.FALSE);
			if (ObjectUtils.isEmpty(nonce) && session != null) {
				nonce = session.getId();
			}
		}
		return nonce;
	}
}