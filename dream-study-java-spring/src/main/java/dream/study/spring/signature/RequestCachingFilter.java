package dream.study.spring.signature;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * 解决请求体只能读取一次
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:46:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ConditionalOnBean(SignatureProperties.class)
@Component
public class RequestCachingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		boolean isFirstRequest = !isAsyncDispatch(request);
		HttpServletRequest requestWrapper = request;
		if (isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
			requestWrapper = new ContentCachingRequestWrapper(request);
		}
		filterChain.doFilter(requestWrapper, response);
	}
}