package dream.study.authorization.server.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import dream.flying.flower.autoconfigure.redis.helper.RedisStrHelpers;
import dream.flying.flower.framework.security.vo.CaptchaVO;
import dream.flying.flower.result.Result;
import dream.study.authorization.server.constant.ConstAuthorizationServerRedis;
import lombok.RequiredArgsConstructor;

/**
 * 获取验证码
 *
 * @author 飞花梦影
 * @date 2024-09-20 17:20:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

	private final RedisStrHelpers redisStrHelpers;

	@GetMapping("/getSmsCaptcha")
	public Result<String> getSmsCaptcha(String phone) {
		// 示例项目,固定1234
		String smsCaptcha = "1234";
		// 存入缓存中,5分钟后过期
		redisStrHelpers.setExpire(ConstAuthorizationServerRedis.SMS_CAPTCHA_PREFIX_KEY + phone, smsCaptcha,
				ConstAuthorizationServerRedis.DEFAULT_TIMEOUT_SECONDS);
		return Result.ok("获取短信验证码成功.", smsCaptcha);
	}

	@GetMapping("/getCaptcha")
	public Result<CaptchaVO> getCaptcha() {
		// 定义图形验证码的长、宽、验证码字符数、干扰线宽度
		ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(130, 34, 4, 2);
		// 生成一个唯一id
		long id = IdWorker.getId();
		// 存入缓存中,5分钟后过期
		redisStrHelpers.setExpire(ConstAuthorizationServerRedis.IMAGE_CAPTCHA_PREFIX_KEY + id, captcha.getCode(),
				ConstAuthorizationServerRedis.DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
		return Result.ok("获取验证码成功",
				CaptchaVO.builder()
						.captchaId(String.valueOf(id))
						.code(captcha.getCode())
						.imageData(captcha.getImageBase64Data())
						.build());
	}
}