package com.wy.crl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.wy.model.CaptchaVO;
import com.wy.service.CaptchaService;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-16 17:43:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

	@Autowired
	private DefaultKaptcha defaultKaptcha;

	@Autowired
	private CaptchaService captchaService;

	@ResponseBody
	@GetMapping("/get")
	public CaptchaVO getCaptcha() throws IOException {

		// 生成文字验证码
		String content = defaultKaptcha.createText();
		// 生成图片验证码
		ByteArrayOutputStream outputStream = null;
		BufferedImage image = defaultKaptcha.createImage(content);

		outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", outputStream);

		String str = "data:image/jpeg;base64,";
		String base64Img = str
				+ Base64.getEncoder().encodeToString(outputStream.toByteArray()).replace("\n", "").replace("\r", "");

		CaptchaVO captchaVO = captchaService.cacheCaptcha(content);
		captchaVO.setBase64Img(base64Img);

		return captchaVO;
	}
}