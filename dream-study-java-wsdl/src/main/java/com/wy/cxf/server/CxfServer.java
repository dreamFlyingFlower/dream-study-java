package com.wy.cxf.server;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.wy.cxf.service.CxfService;
import com.wy.wsdl.model.User;

/**
 * cxf接口实现类
 * 
 * @author ParadiseWY
 * @date 2020-10-14 23:19:43
 * @git {@link https://github.com/mygodness100}
 */
@WebService
@Component
public class CxfServer implements CxfService {

	@Override
	public String getUsername(String username) {
		return "cxf调用:" + username;
	}

	@Override
	public User getUser(User model) {
		return null;
	}
}