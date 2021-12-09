package com.wy.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * Shiro自定义密码认证
 *
 * @author 飞花梦影
 * @date 2021-04-17 17:54:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class CredentialMatcher extends SimpleCredentialsMatcher {

	/**
	 * 检查用户授权信息中的密码和认证信息中的密码是否相同
	 * 
	 * @param token 用户
	 * @param info 授权信息
	 * @return true->匹配,false->不匹配
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		if (token instanceof UsernamePasswordToken) {
			char[] password = ((UsernamePasswordToken) token).getPassword();
			String dbPwd = String.valueOf(info.getCredentials());
			return this.equals(new String(password), dbPwd);
		}
		return false;
	}
}