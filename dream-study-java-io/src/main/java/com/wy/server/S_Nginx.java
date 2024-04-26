package com.wy.server;

/**
 * Nginx
 * 
 * 实现负载均衡:可以再配置文件中添加upstream,多server,多日志配置,shell对日志分割
 * 
 * @author 飞花梦影
 * @date 2022-05-07 16:37:28
 * @git {@link https://github.com/mygodness100}
 */
public class S_Nginx {

	/**
	 * nginx命令:start nginx.exe,nginx.exe -s stop,nginx.exe -s reload 正向代理:用户->代理->浏览器.浏览器可以很明确的知道访问他的用户地址
	 * 反响代理:用户->nginx->tomcat.tomcat不知道请求的真正来源,只知道是从nginx发送的请求
	 */
}