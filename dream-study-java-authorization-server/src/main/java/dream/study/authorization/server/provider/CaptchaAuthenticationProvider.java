package dream.study.authorization.server.provider;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import dream.flying.flower.autoconfigure.redis.helper.RedisStrHelpers;
import dream.flying.flower.framework.security.constant.ConstAuthorization;
import dream.flying.flower.result.ResultException;
import dream.study.authorization.server.constant.ConstAuthorizationServerRedis;
import lombok.extern.slf4j.Slf4j;

/**
 * 验证码校验.如注入该类的子类,会替换默认的DaoAuthenticationProvider
 *
 * @author 飞花梦影
 * @date 2024-09-18 22:19:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class CaptchaAuthenticationProvider extends DaoAuthenticationProvider {

	private final RedisStrHelpers redisStrHelpers;

	/**
	 * 注入UserDetailsService和passwordEncoder
	 *
	 * @param userDetailsService 用户服务,给框架提供用户信息
	 * @param passwordEncoder 密码解析器,用于加密和校验密码
	 */
	public CaptchaAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
			RedisStrHelpers redisStrHelpers) {
		this.redisStrHelpers = redisStrHelpers;
		super.setPasswordEncoder(passwordEncoder);
		super.setUserDetailsService(userDetailsService);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("Authenticate captcha...");

		// 获取当前request
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			throw new ResultException("Failed to get the current request.");
		}
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

		// 获取当前登录方式
		String loginType = request.getParameter(ConstAuthorization.LOGIN_TYPE_NAME);
		if (!Objects.equals(loginType, ConstAuthorization.PASSWORD_LOGIN_TYPE)) {
			// 只要不是密码登录都不需要校验图形验证码
			log.info("It isn't necessary captcha authenticate.");
			return super.authenticate(authentication);
		}

		// 获取参数中的验证码
		String code = request.getParameter(ConstAuthorization.CAPTCHA_CODE_NAME);
		if (ObjectUtils.isEmpty(code)) {
			throw new ResultException("The captcha cannot be empty.");
		}

		String captchaId = request.getParameter(ConstAuthorization.CAPTCHA_ID_NAME);
		// 获取缓存中存储的验证码
		String captchaCode = redisStrHelpers.get(ConstAuthorizationServerRedis.IMAGE_CAPTCHA_PREFIX_KEY + captchaId);
		if (!ObjectUtils.isEmpty(captchaCode)) {
			if (!captchaCode.equalsIgnoreCase(code)) {
				throw new ResultException("The captcha is incorrect.");
			}
		} else {
			throw new ResultException("The captcha is abnormal. Obtain it again.");
		}

		// 删除缓存
		redisStrHelpers.delete(ConstAuthorizationServerRedis.IMAGE_CAPTCHA_PREFIX_KEY + captchaId);
		log.info("Captcha authenticated.");
		return super.authenticate(authentication);
	}
}