
package com.wy.shiro.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.wy.lang.StrTool;

import lombok.extern.log4j.Log4j2;

/**
 * 网络地址工具类
 */
@Log4j2
public class IpAddrUtil {

	/**
	 * 获取网络用户真实地址
	 */
	public static String requestIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {

			ipAddress = request.getHeader("X-Real-IP");
			if (StrTool.isBlank(ipAddress)) {
				ipAddress = request.getRemoteAddr();
			}
			if (StrTool.isNotBlank(ipAddress)) {
				if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}
		}
		// 对于通过多个代理的情况,第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		log.info("登录IP地址:" + ipAddress);
		return ipAddress;
	}

	/**
	 * 获取请求主机IP地址,如果通过代理进来,则透过防火墙获取真实IP地址;
	 *
	 * @param request
	 * @return
	 */
	public final static String getIpAddress(HttpServletRequest request) {
		// 获取请求主机IP地址,如果通过代理进来,则透过防火墙获取真实IP地址
		String ip = request.getHeader("X-Forwarded-For");
		if (log.isInfoEnabled()) {
			log.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			if (log.isInfoEnabled()) {
				log.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (log.isInfoEnabled()) {
					log.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
				if (log.isInfoEnabled()) {
					log.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (log.isInfoEnabled()) {
					log.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
				if (log.isInfoEnabled()) {
					log.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
				}
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 获得当地主机Ip
	 */
	public static String hostIpAddr() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			log.error("本机IP地址获取异常！");
		}
		if (Objects.nonNull(address)) {
			return address.getHostAddress();
		} else {
			return null;
		}
	}
}