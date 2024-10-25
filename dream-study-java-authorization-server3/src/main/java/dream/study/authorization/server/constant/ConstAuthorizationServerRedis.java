package dream.study.authorization.server.constant;

/**
 * Redis相关常量
 * 
 * @auther 飞花梦影
 * @date 2019-09-23 00:10:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstAuthorizationServerRedis {

	/**
	 * 认证信息存储前缀
	 */
	String SECURITY_CONTEXT_PREFIX_KEY = "security_context:";

	/**
	 * 短信验证码前缀
	 */
	String SMS_CAPTCHA_PREFIX_KEY = "mobile_phone:";

	/**
	 * 图形验证码前缀
	 */
	String IMAGE_CAPTCHA_PREFIX_KEY = "image_captcha:";

	/**
	 * 默认过期时间,默认五分钟
	 */
	long DEFAULT_TIMEOUT_SECONDS = 60L * 5;
}