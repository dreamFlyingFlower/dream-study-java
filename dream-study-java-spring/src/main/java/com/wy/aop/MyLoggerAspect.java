package com.wy.aop;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wy.annotation.Logger;
import com.wy.model.SysLog;
import com.wy.service.SysLogService;

/**
 * 日志注解拦截
 *
 * @author 飞花梦影
 * @date 2021-12-09 16:51:12
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Component
@Aspect
public class MyLoggerAspect {

	@Autowired
	private SysLogService sysLogService;

	/**
	 * 注解Pointcut切入点
	 */
	@Pointcut("@annotation(com.wy.annotation.Logger)")
	public void loggerAspect() {
	}

	/**
	 * 抛出异常后通知:方法抛出异常退出时执行的通知,在这里不能使用ProceedingJoinPoint
	 * 
	 * @param joinPoint 切入点
	 * @param throwable 异常信息
	 */
	@AfterThrowing(value = "loggerAspect()", throwing = "throwable")
	public void afterThrowingMethod(JoinPoint joinPoint, Throwable throwable) throws Exception {
		HttpServletRequest httpServletRequest = getHttpServletRequest();
		// 获取管理员用户信息
		Map<String, Object> user = getUser(httpServletRequest);
		SysLog log = new SysLog();
		// 获取需要的信息
		String context = getServiceMthodDescription(joinPoint);
		String usr_name = "";
		String rolename = "";
		if (user != null) {
			usr_name = user.get("usr_name").toString();
			rolename = user.get("rolename").toString();
		}
		// 管理员姓名
		log.setUserName(usr_name);
		// 角色名
		log.setUserRole(rolename);
		// 日志信息
		log.setContent(usr_name + context);
		// 设置参数集合
		log.setRemarks(getServiceMthodParams(joinPoint));
		// 设置表名
		log.setTableName(getServiceMthodTableName(joinPoint));
		// 操作时间
		SimpleDateFormat sif = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.setDateTime(sif.format(new Date()));
		// 设置ip地址
		log.setIp(httpServletRequest.getRemoteAddr());
		// 设置请求地址
		log.setRequestUrl(httpServletRequest.getRequestURI());
		// 执行结果
		log.setResult("执行失败");
		// 错误信息
		log.setExString(throwable.getMessage());
		sysLogService.create(log);
	}

	/**
	 * 返回后通知:正常完成后执行的通知,在这里不能使用ProceedingJoinPoint
	 * 
	 * @param joinPoint
	 * @param returnValue 返回值
	 * @throws Exception
	 */
	@AfterReturning(value = "loggerAspect()", returning = "returnValue")
	public void doCrmLog(JoinPoint joinPoint, Object returnValue) throws Exception {
		HttpServletRequest httpServletRequest = getHttpServletRequest();
		// 获取管理员用户信息
		Map<String, Object> user = getUser(httpServletRequest);
		SysLog log = new SysLog();
		String context = getServiceMthodDescription(joinPoint);
		String usr_name = "";
		String rolename = "";
		if (user != null) {
			usr_name = user.get("usr_name").toString();
			rolename = user.get("rolename").toString();
		}
		// 管理员姓名
		log.setUserName(usr_name);
		// 角色名
		log.setUserRole(rolename);
		// 日志信息
		log.setContent(usr_name + context);
		// 设置参数集合
		log.setRemarks(getServiceMthodParams(joinPoint));
		// 设置表名
		log.setTableName(getServiceMthodTableName(joinPoint));
		// 操作时间
		SimpleDateFormat sif = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.setDateTime(sif.format(new Date()));
		// 设置ip地址
		log.setIp(httpServletRequest.getRemoteAddr());
		// 设置请求地址
		log.setRequestUrl(httpServletRequest.getRequestURI());
		log.setResult("执行成功");
		log.setResultValue(JSON.toJSONString(returnValue));
		sysLogService.create(log);
	}

	/**
	 * 获取自定义注解里的日志描述
	 * 
	 * @param joinPoint
	 * @return 返回注解里面的日志描述
	 * @throws Exception
	 */
	private String getServiceMthodDescription(JoinPoint joinPoint) throws Exception {
		// 类名
		String targetName = joinPoint.getTarget().getClass().getName();
		// 方法名
		String methodName = joinPoint.getSignature().getName();
		// 参数
		Object[] arguments = joinPoint.getArgs();
		// 通过反射获取示例对象
		Class<?> targetClass = Class.forName(targetName);
		// 通过实例对象方法数组
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods) {
			// 判断方法名是不是一样
			if (method.getName().equals(methodName)) {
				// 对比参数数组的长度
				Class<?>[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					// 获取注解里的日志信息
					description = method.getAnnotation(Logger.class).description();
					break;
				}
			}
		}
		return description;
	}

	/**
	 * 获取自定义注解里的表名
	 * 
	 * @param joinPoint
	 * @return 返回注解里的表名字
	 * @throws Exception
	 */
	private String getServiceMthodTableName(JoinPoint joinPoint) throws Exception {
		// 类名
		String targetName = joinPoint.getTarget().getClass().getName();
		// 方法名
		String methodName = joinPoint.getSignature().getName();
		// 参数
		Object[] arguments = joinPoint.getArgs();
		// 通过反射获取示例对象
		Class<?> targetClass = Class.forName(targetName);
		// 通过实例对象方法数组
		Method[] methods = targetClass.getMethods();
		// 表名
		String tableName = "";
		for (Method method : methods) {
			// 判断方法名是不是一样
			if (method.getName().equals(methodName)) {
				// 对比参数数组的长度
				Class<?>[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					// 获取注解里的表名
					tableName = method.getAnnotation(Logger.class).tableName();
					break;
				}
			}
		}
		return tableName;
	}

	/**
	 * 获取json格式的参数用于存储到数据库中
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	private String getServiceMthodParams(JoinPoint joinPoint) throws Exception {
		Object[] arguments = joinPoint.getArgs();
		ObjectMapper om = new ObjectMapper();
		return om.writeValueAsString(arguments);
	}

	/**
	 * 获取当前的request 这里如果报空指针异常是因为单独使用spring获取request 需要在配置文件里添加监听 <listener>
	 * <listener-class>
	 * org.springframework.web.context.request.RequestContextListener
	 * </listener-class> </listener>
	 * 
	 * @return
	 */
	public HttpServletRequest getHttpServletRequest() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		return request;
	}

	/**
	 * 从session或redis中获取到用户对象
	 * 
	 * @return
	 */
	public Map<String, Object> getUser(HttpServletRequest request) {
		return new HashMap<>();
	}
}