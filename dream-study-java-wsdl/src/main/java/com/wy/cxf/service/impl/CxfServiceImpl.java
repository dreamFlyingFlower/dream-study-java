package com.wy.cxf.service.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.wy.cxf.service.CxfService;
import com.wy.wsdl.model.User;

/**
 * CXF接口实现类
 * 
 * {@link WebService}:该注解必须有,否则报错.
 * {@link WebService#serviceName}:与接口中指定的name一致,可不写
 * {@link WebService#targetNamespace}:与接口中的命名空间一致,一般是接口的包名倒.可不写
 * {@link WebService#endpointInterface}:接口的完整路径地址,可不写
 * 
 * @author ParadiseWY
 * @date 2020-10-14 23:19:43
 * @git {@link https://github.com/mygodness100}
 */
@WebService(serviceName = "wsdlService", targetNamespace = "com.wy.wsdl", endpointInterface = "com.wy.cxf.service")
@Component
public class CxfServiceImpl implements CxfService {

	@Override
	public String getUsername(String username) {
		return "cxf调用:" + username;
	}

	@Override
	public User getUser(User model) {
		return null;
	}
}