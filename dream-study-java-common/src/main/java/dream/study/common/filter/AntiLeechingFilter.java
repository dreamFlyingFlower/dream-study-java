package dream.study.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * 防盗链拦截,不建议使用,直接使用nginx的防盗链功能
 *
 * @author 飞花梦影
 * @date 2025-12-30 13:09:01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@Component
@EnableConfigurationProperties(AntiLeechingProperties.class)
public class AntiLeechingFilter extends OncePerRequestFilter {

	private AntiLeechingProperties antiLeechingProperties;

	public AntiLeechingFilter(AntiLeechingProperties antiLeechingProperties) {
		this.antiLeechingProperties = antiLeechingProperties;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 如果防盗链未启用,直接放行
		if (!antiLeechingProperties.isEnabled()) {
			filterChain.doFilter(request, response);
			return;
		}

		// 获取Referer
		String referer = request.getHeader("referer");
		log.debug("Referer: {}", referer);

		// 检查是否为白名单路径
		if (isWhitelistPath(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}

		// 检查是否允许直接访问
		if (antiLeechingProperties.isAllowDirectAccess() && (referer == null || referer.isEmpty())) {
			filterChain.doFilter(request, response);
			return;
		}

		// 检查Referer是否在白名单中
		boolean isAllowed = false;
		for (String domain : antiLeechingProperties.getAllowedDomains()) {
			if (domain.equals("none") && referer == null) {
				isAllowed = true;
				break;
			}
			if (domain.equals("blocked") && (referer == null || referer.isEmpty())) {
				isAllowed = true;
				break;
			}
			if (domain.startsWith("*.") && referer != null && referer.contains(domain.substring(2))) {
				isAllowed = true;
				break;
			}
			if (domain.matches("^~\\..*\\..*$") && referer != null && referer.matches(domain.substring(1))) {
				isAllowed = true;
				break;
			}
			if (referer != null && referer.contains(domain)) {
				isAllowed = true;
				break;
			}
		}

		// 如果不在白名单,处理拒绝
		if (!isAllowed) {
			handleDenyAction(response, antiLeechingProperties.getDefaultImage());
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isWhitelistPath(String uri) {
		// 白名单路径配置,如/api/public/**
		return false;
	}

	@SuppressWarnings("resource")
	private void handleDenyAction(HttpServletResponse request, String defaultImage) {
		// 根据deny-action配置决定处理方式
		if ("REDIRECT".equals(antiLeechingProperties.getDenyAction())) {
			try {
				request.sendRedirect(defaultImage);
			} catch (IOException e) {
				log.error("Redirect failed", e);
			}
		} else if ("FORBIDDEN".equals(antiLeechingProperties.getDenyAction())) {
			request.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else if ("DEFAULT_IMAGE".equals(antiLeechingProperties.getDenyAction())) {
			try {
				request.setContentType("image/png");
				request.getOutputStream().write(getImageBytes(defaultImage));
			} catch (IOException e) {
				log.error("Failed to send default image", e);
			}
		}
	}

	private byte[] getImageBytes(String path) {
		// 读取默认图片的字节数据
		return new byte[0];
	}
}