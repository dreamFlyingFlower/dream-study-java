package com.wy.crl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.util.DateTimeTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "支付后台")
@Controller
@RequestMapping(value = "pay")
public class PayCrl {

	private static final Logger logger = LoggerFactory.getLogger(PayCrl.class);

	@ApiOperation("登陆")
	@PostMapping("login")
	public @ResponseBody String login(HttpServletRequest request, HttpServletResponse response, String account,
			String password) throws Exception {
		logger.info("登陆");
		String param = "false";
		if ("admin".equals(account) && "111111".equals(password)) {
			param = "true";
		}
		return param;
	}

	@ApiOperation("后台展示")
	@GetMapping("main")
	public String main(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		model.addAttribute("ip", "192.168.1.66");
		model.addAttribute("address", "青岛");
		model.addAttribute("time", DateTimeTool.formatDateTime());
		return "web/main";
	}
}