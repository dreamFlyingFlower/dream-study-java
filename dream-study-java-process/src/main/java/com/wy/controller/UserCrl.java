package com.wy.controller;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.wy.base.AbstractCrl;
import com.wy.model.User;
import com.wy.result.Result;
import com.wy.service.UserService;
import com.wy.utils.StrUtils;

@RestController
@RequestMapping("user")
public class UserCrl extends AbstractCrl<User> {

	@Autowired
	private UserService userService;

	@Override
	public Result<?> create(@RequestBody User user, BindingResult bind) {
		if (bind.hasErrors()) {
			FieldError error = bind.getFieldError();
			assert error != null;
			return Result.error(error.getField() + error.getDefaultMessage());
		}
		user = User.builder().username("test05").password("12345678").email("123456@qq.com").mobile("13456789096")
				.gender("m").createtime(new Date()).build();
		return Result.ok(userService.insertSelective(user));
	}

	@GetMapping("checkUsername")
	public Result<?> checkUsername(String username) {
		if (StrUtils.isBlank(username)) {
			return Result.error("参数错误");
		}
		return Result.result(userService.checkUsername(username));
	}

	@GetMapping("login")
	public Result<?> login(String username, String password) {
		if (StrUtils.isBlank(password) || StrUtils.isBlank(username)) {
			return Result.error("用户名或密码不能为空");
		}
		return Result.ok(userService.login(username, password));
	}

	@RequestMapping(value = "/fileload")
	public Result<?> fileload(HttpServletRequest request) {
		CommonsMultipartResolver multi = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multi.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					String path = "D:/springUpload" + file.getOriginalFilename();
					// 上传
					try {
						file.transferTo(new File(path));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return Result.ok(null);
	}
}