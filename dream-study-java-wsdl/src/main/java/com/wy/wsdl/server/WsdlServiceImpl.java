package com.wy.wsdl.server;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.wy.wsdl.model.User;
import com.wy.wsdl.service.WsdlService;

/**
 * WebService可调用服务
 * 
 * @apiNote 当springboot整合cxf时该接口才需要添加Componet注解,原生jdk的webservice不需要加
 *
 * @author ParadiseWY
 * @date 2019-02-28 13:36:32
 * @git {@link https://github.com/mygodness100}
 */
@WebService
@Component
public class WsdlServiceImpl implements WsdlService {

	@Override
	public String getUsername(String username) {
		return "success:" + username;
	}

	@Override
	public User getUser(User model) {
		System.out.println(model.getUsername());
		User user = new User();
		user.setUserId(1);
		user.setUsername(model.getUsername() + ":sucsess");
		user.setAddress("测试地址");
		return user;
	}
}